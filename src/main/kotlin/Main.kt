package org.godker

import okhttp3.OkHttpClient
import org.godker.utils.CryptoService
import org.godker.utils.PropertiesReader
import org.godker.utils.SimpleXmlSerializer
import org.godker.soap.SoapClient

fun main() {

    println(PropertiesReader.getProperty("CERT"))

    val serializer = SimpleXmlSerializer()

    val crypto = CryptoService(
        PropertiesReader.getProperty("CERT"),
        PropertiesReader.getProperty("KEY")
    )

    val httpClient = OkHttpClient()
    val soapClient = SoapClient(PropertiesReader.getProperty("URL"), httpClient)

    val wsaa = WSAA(AFIPWS.WSFEv1, crypto, serializer, soapClient)
    val tra = wsaa.llamarWSAA()

    println(tra)
}