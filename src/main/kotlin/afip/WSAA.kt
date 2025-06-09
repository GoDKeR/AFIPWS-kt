package org.godker.afip

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.godker.utils.CryptoService
import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope
import org.godker.afip.AFIPServices
import org.godker.utils.PropertiesReader
import org.godker.utils.getMap
import org.godker.utils.xmlToMap
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
class WSAA (private val service: AFIPServices, private val crypto: CryptoService, private val soapClient: SoapClient) {

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

        val xmlRequest = """
            <loginTicketRequest>
                <header>
                    <uniqueId>${uniqueId}</uniqueId>
                    <generationTime>${generationTime}</generationTime>
                    <expirationTime>${expirationTime}</expirationTime>
                </header>
                <service>${service.value}</service>
            </loginTicketRequest>""".trimIndent()

        return xmlRequest
    }

    private fun validarCache(): Pair<Boolean, LoginTicketCredentials?> {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        val file = File(fileName)

        return file.takeIf { it.exists() }?.readLines()?.joinToString("")?.let { content ->
            val response = content.trim().xmlToMap() ?: return@let (false to null)
            val header = response.getMap("header") ?: return@let (false to null)
            val credentials = response.getMap("credentials") ?: return@let (false to null)

            val expired = Instant.parse(header["expirationTime"] as String) < Clock.System.now()

            val ltc = LoginTicketCredentials(credentials["token"] as String, credentials["sign"] as String)

            if (expired) false to null else true to ltc
        } ?: (false to null)
    }

    private fun escribirCache(response: Map<String, Any?>) {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        val xml = """
            <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
            <loginTicketResponse version="1.0">
                <header>
                    <source>${response.getMap("header")!!["source"]}</source>
                    <destination>${response.getMap("header")!!["destination"]}</destination>
                    <uniqueId>${response.getMap("header")!!["uniqueId"]}</uniqueId>
                    <generationTime>${response.getMap("header")!!["generationTime"]}</generationTime>
                    <expirationTime>${response.getMap("header")!!["expirationTime"]}</expirationTime>
                </header>
                <credentials>
                    <token>${response.getMap("credentials")!!["token"]}</token>
                    <sign>${response.getMap("credentials")!!["sign"]}</sign>
                </credentials>
            </loginTicketResponse>
        """.trimIndent()
        File(fileName).writeText(xml)
    }

    fun limpiarCache(): Boolean {
        val fileName = "${PropertiesReader.getProperty("CACHE_DIR")}TA-${service.value}.xml"
        return Path(fileName).deleteIfExists()
    }

    fun llamarWSAA(): LoginTicketCredentials {

        return validarCache().let { result ->
            (if (result.first) {
                println("TA válido.")
                result.second
            }else {
                println("TA vencido. Solicitando uno nuevo...")
                val traXml = crearTRA()
                val cms = crypto.sign(traXml)
                val cmsBase64 = Base64.encode(cms)

                val soapEnvelope = SoapEnvelope(
                    mapOf(
                        "wsaa:loginCms" to mapOf(
                            "wsaa:in0" to cmsBase64
                        )
                    )
                )
                soapEnvelope.registerNamespace("wsaa", "http://wsaa.view.sua.dvadac.desein.afip.gov")

                val endpoint = PropertiesReader.getProperty("WSAA_URL_HOMOLOGACION")

                val builded = soapEnvelope.build()

                val cmsRequestResult = soapClient.sendSoapRequest(endpoint, builded)
                
                val (header, credentials) = run {
                    val soapResult = cmsRequestResult.xmlToMap()
                    val body = soapResult?.getMap("soapenv:Body")
                    val loginCms = body?.getMap("loginCmsResponse")
                    val loginReturn = (loginCms?.get("loginCmsReturn") as? String)?.xmlToMap()
                    val header = loginReturn?.getMap("header")
                    val credentials = loginReturn?.getMap("credentials")

                    if (soapResult == null || body == null || loginCms == null || loginReturn == null || header == null || credentials == null)
                        throw RuntimeException("Fallo al leer la respuesta del CMS.")

                    escribirCache(loginReturn)

                    header to credentials
                }

                println("TA recibido. Válido hasta: ${header["expirationDate"]}")

                LoginTicketCredentials(credentials["token"] as String, credentials["sign"] as String)

            })as LoginTicketCredentials
        }
    }
}