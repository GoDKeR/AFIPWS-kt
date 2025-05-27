package org.godker

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import okhttp3.OkHttpClient
import org.godker.utils.CryptoService
import org.godker.utils.PropertiesReader
import org.godker.utils.SimpleXmlSerializer
import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope
import org.godker.soap.wsaa.In0Wrapper
import org.godker.soap.wsaa.LoginCmsRequest
import org.godker.soap.wsfe.CbteAsocWrapper
import org.godker.soap.wsfe.ComprobanteAsociado
import org.godker.soap.wsfe.FEAuth
import org.godker.soap.wsfe.FECAECabReq
import org.godker.soap.wsfe.FECAEDetReq
import org.godker.soap.wsfe.FECAEReq
import org.godker.soap.wsfe.FECAESolicitar
import org.godker.soap.wsfe.FeCAEDetItem
import org.godker.utils.AFIPServices
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

/*
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
 */

class Cli() : CliktCommand() {
    override fun help(context: Context): String = """
    Cliente para acceder a los Webservices de AFIP.    
    """.trimIndent()

    override fun run() {
        //TODO("Not yet implemented")

        /*val serializer = SimpleXmlSerializer()

        val sol = FECAESolicitar(
            FEAuth("tokenwi", "signwi", 88),
            FECAEReq(
                FECAECabReq(1,2,3),
                FECAEDetReq(mutableListOf<FeCAEDetItem>(
                    FeCAEDetItem(1, 2,38334557, 3, 4, "", 10.00, 11.00, 12.00, 0.00, 0.00, 0.01, "", "", "", "1", 0.00, "", 1,
                        CbteAsocWrapper(
                            arrayListOf(
                                ComprobanteAsociado(1, 2, 3, "", ""),
                                ComprobanteAsociado(1, 2, 3, "", "")
                            )
                        )
                    )
                ))
            ))
        println(SoapEnvelope.toXml(serializer.toXml(sol)).trimIndent())*/

        val serializer = SimpleXmlSerializer()

        val crypto = CryptoService(
            PropertiesReader.getProperty("CERT"),
            PropertiesReader.getProperty("KEY")
        )

        val httpClient = OkHttpClient()
        val soapClient = SoapClient(httpClient)

        val wsaa = WSAA(AFIPServices.WSFEv1, crypto, serializer, soapClient)

        println(wsaa.llamarWSAA())
    }

}

fun main(args: Array<String>) = Cli().main(args)