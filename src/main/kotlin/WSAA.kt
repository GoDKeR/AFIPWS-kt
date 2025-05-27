package org.godker

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.godker.soap.wsaa.In0Wrapper
import org.godker.soap.wsaa.LoginTicketRequest
import org.godker.soap.wsaa.LoginTicketRequestHeader
import org.godker.utils.CryptoService
import org.godker.utils.SimpleXmlSerializer
import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope
import org.godker.soap.wsaa.LoginCmsRequest
import org.godker.soap.wsaa.LoginCmsResponse
import org.godker.soap.wsaa.LoginTicketResponse
import org.godker.soap.wsaa.LoginTicketResponseCredentials
import org.godker.utils.AFIPServices
import org.godker.utils.PropertiesReader
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists

/*
* Clase del Web Service de Autenticación y Autorización (WSAA)
*
* https://www.arca.gob.ar/ws/WSAA/WSAAmanualDev.pdf
*
*/

@OptIn(ExperimentalEncodingApi::class)
class WSAA (private val service: AFIPServices, private val crypto: CryptoService, private val xmlSerializer: SimpleXmlSerializer, private val soapClient: SoapClient) {

    private fun crearTRA(): String {
        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        val uniqueId = "%02d%02d%02d".format(date.year % 100, date.monthNumber, date.dayOfMonth)
        val generationTime = "%04d-%02d-%02dT%02d:%02d:%02d".format(
            date.year, date.monthNumber, date.dayOfMonth,
            date.hour, date.minute, date.second
        )

        val expirationDate = date.toInstant(TimeZone.currentSystemDefault())
            .plus(20, DateTimeUnit.MINUTE)
            .toLocalDateTime(TimeZone.currentSystemDefault())

        val expirationTime = "%04d-%02d-%02dT%02d:%02d:%02d".format(
            expirationDate.year, expirationDate.monthNumber, expirationDate.dayOfMonth,
            expirationDate.hour, expirationDate.minute, expirationDate.second
        )

        val request = LoginTicketRequest(
            header = LoginTicketRequestHeader(uniqueId, generationTime, expirationTime),
            service = service.value
        )

        return xmlSerializer.toXml(request)
    }

    private fun validarCache(): Pair<Boolean, LoginTicketResponseCredentials?> {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        val file = File(fileName)

        return file.takeIf { it.exists() }?.readText()?.let { content ->
            val response = xmlSerializer.fromXml(
                content,
                LoginTicketResponse::class.java
            )

            val expired = Instant.parse(response.header.expirationTime) < Clock.System.now()

            if (expired) false to null else true to response.credentials
        } ?: (false to null)
    }

    private fun escribirCache(xml: String) {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        File(fileName).writeText(xml)
    }

    fun limpiarCache(): Boolean {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        return Path(fileName).deleteIfExists()
    }

    fun llamarWSAA(): LoginTicketResponseCredentials {

        return validarCache().let { result ->
            (if (result.first)
                result.second
            else {
                val traXml = crearTRA()
                val cms = crypto.sign(traXml)
                val cmsBase64 = Base64.encode(cms)

                val soapBody = xmlSerializer.toXml(
                    LoginCmsRequest(
                        In0Wrapper(
                            cmsBase64
                        )
                    )
                )

                val endpoint = PropertiesReader.getProperty("WSAA_URL_HOMOLOGACION")
                val cmsRequestResult = soapClient.sendSoapRequest(endpoint, SoapEnvelope.toXml(soapBody))

                val soapResult = xmlSerializer.fromXml(cmsRequestResult, LoginCmsResponse::class.java)

                //TODO validar errores etc

                var xmlResult = soapResult.body!!.loginCmsResponse!!.data!!.data

                escribirCache(xmlResult)

                xmlSerializer.fromXml(xmlResult, LoginTicketResponse::class.java)

            }) as LoginTicketResponseCredentials
        }
    }
}