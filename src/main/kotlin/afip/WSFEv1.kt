package org.godker.afip

import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope
import org.godker.utils.PropertiesReader

/*
* Clase del Web Service para Facturación Electrónica (WSFE)
*
* https://www.afip.gob.ar/ws/documentacion/manuales/manual-desarrollador-ARCA-COMPG-v4-0.pdf
*
*/

class WSFEv1(private val credentials: LoginTicketCredentials, private val soapClient: SoapClient) {

    private val baseActionUrl = "http://ar.gov.afip.dif.FEV1/"
    private val auth = mapOf(
        "ar:Token" to credentials.token,
        "ar:Sign" to credentials.sign,
        "ar:Cuit" to PropertiesReader.getProperty("CUIT")
    )

    //region Para CAE - RG 4291
    //TODO factura -> Array
    fun feCAESolicitar(factura: Factura) {
        val endpoint = "${PropertiesReader.getProperty("WSFE_URL_HOMOLOGACION")}?op=${WSFEOperation.FECAESolicitar}"

        val soapEnvelope = SoapEnvelope(
            mapOf(
                "ar:${WSFEOperation.FECAESolicitar}" to mapOf(
                    "ar:Auth" to auth,
                    "ar:FeCAEReq" to mapOf(
                        "ar:FeCabReq" to mapOf(
                            "ar:CantReg" to 1, //TODO: count array factura
                            "ar:PtoVta" to 1, //TODO: ptovta
                            "ar:CbteTipo" to 1 //Todo cbtetipo
                        ),
                        "ar:FeDetReq" to factura
                    )
                )
            )
        )
        soapEnvelope.registerNamespace("ar", "http://ar.gov.afip.dif.FEV1/")

        val xmlRequest = soapEnvelope.build() //TODO: check if needed to persist

        println(xmlRequest)

        val result = soapClient.sendSoapRequest(
            endpoint,
            xmlRequest,
            baseActionUrl + WSFEOperation.FECAESolicitar
        )

        val response = SoapEnvelope(result)["FECAESolicitarResponse"] as? Map<*, *> ?: throw IllegalArgumentException("FECAESolicitarResponse no existe.")
        val responseBody = response["FECAESolicitarResult"] as? Map<String, Any?> ?: throw IllegalArgumentException("FECAESolicitarResult no existe.")

        (responseBody["Errors"] as? Map<*,*>)?.let { error ->
            val errorList = error["Err"]
            println("La solicitud de CAE falló con los siguientes errores:")

            when(errorList){
                is List<*> -> {
                    for (item in errorList){
                        val errItem = item as? Map<*,*> ?: continue
                        println("\t${errItem["Code"]}: ${errItem["Msg"]}")
                    }
                }

                is Map<*,*> -> {
                    println("\t${errorList["Code"]}: ${errorList["Msg"]}")
                }

                else -> {
                    println("Formato de error no reconocido.")
                }
            }
        }

        println(result)
    }
    //endregion

    //region Para CAEA - RG 4291
    fun feCAEASolicitar() {

    }

    fun feCAEAConsultar() {

    }
    //endregion

    fun feParamGetTiposCbte() {

    }

    fun feParamGetTiposConcepto() {

    }

    fun feParamGetTiposDoc() {

    }

    fun feParamGetTiposIVA() {

    }

    fun feParamGetTiposMoneda() {

    }

    fun feParamGetTiposOpcional() {

    }

    fun feParamGetTiposTributos() {

    }

    fun feParamGetPtosVenta() {

    }

    fun feParamGetCotizacion() {

    }

    fun feDummy() {

    }

    fun feCompUltimoAutorizado() {

    }

    fun feCompTotXRequest() {

    }

    fun feCompConsultar() {

    }

    fun feParamGetActividades() {

    }
}