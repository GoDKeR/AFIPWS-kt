package org.godker

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.godker.soap.requests.In0Wrapper
import org.godker.soap.requests.LoginCmsRequest
import org.godker.soap.requests.LoginTicketRequest
import org.godker.soap.requests.LoginTicketRequestHeader
import org.godker.utils.CryptoService
import org.godker.utils.SimpleXmlSerializer
import org.godker.soap.SoapClient
import org.godker.soap.requests.LoginCmsRequestBody
import org.godker.soap.requests.LoginCmsRequestData
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/*
* Clase del Web Service de Autenticación y Autorización (WSAA)
*
* https://www.arca.gob.ar/ws/WSAA/WSAAmanualDev.pdf
 */

@OptIn(ExperimentalEncodingApi::class)
class WSAA (private val service: AFIPWS, private val crypto: CryptoService, private val xmlSerializer: SimpleXmlSerializer, private val soapClient: SoapClient) {

    var Homologacion: Boolean = false
        get() = field
        set(value) {
            field = value
        }

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

    fun llamarWSAA(): String{
        //TODO: cache
        val traXml = crearTRA()
        val cms = crypto.sign(traXml)
        val cmsBase64 = Base64.encode(cms)

        val soapRequest = xmlSerializer.toXml(LoginCmsRequest(body = LoginCmsRequestBody(
            LoginCmsRequestData(
                In0Wrapper(
                    cmsBase64
                )
            )
        )
        ))

        return soapClient.sendSoapRequest(soapRequest)
    }

}