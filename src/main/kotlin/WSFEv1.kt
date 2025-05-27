package org.godker

import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope
import org.godker.soap.wsaa.LoginTicketResponseCredentials
import org.godker.soap.wsfe.*
import org.godker.utils.PropertiesReader
import org.godker.utils.SimpleXmlSerializer

/*
* Clase del Web Service para Facturación Electrónica (WSFE)
*
* https://www.afip.gob.ar/ws/documentacion/manuales/manual-desarrollador-ARCA-COMPG-v4-0.pdf
*
*/

class WSFEv1(private val xmlSerializer: SimpleXmlSerializer, private val soapClient: SoapClient) {


    //region Para CAE - RG 4291
    fun feCAESolicitar(wsaaResp: LoginTicketResponseCredentials, request: FECAEReq){
        val auth = FEAuth(wsaaResp.token, wsaaResp.sign, PropertiesReader.getProperty("CUIT").toLong())
        val endpoint = "${PropertiesReader.getProperty("WSFE_URL_HOMOLOGACION")}?op=${WSFEOperation.FECAESolicitar}"

        val fe = FECAESolicitar(auth, request)

        val result = soapClient.sendSoapRequest(endpoint, SoapEnvelope.toXml(xmlSerializer.toXml(fe)))
    }
    //endregion

    //region Para CAEA - RG 4291
    fun feCAEASolicitar(){

    }

    fun feCAEAConsultar() {

    }
    //endregion

    fun feParamGetTiposCbte(){

    }

    fun feParamGetTiposConcepto(){

    }

    fun feParamGetTiposDoc(){

    }

    fun feParamGetTiposIVA(){

    }

    fun feParamGetTiposMoneda(){

    }

    fun feParamGetTiposOpcional(){

    }

    fun feParamGetTiposTributos(){

    }

    fun feParamGetPtosVenta(){

    }

    fun feParamGetCotizacion(){

    }

    fun feDummy(){

    }

    fun feCompUltimoAutorizado(){

    }

    fun feCompTotXRequest(){

    }

    fun feCompConsultar(){

    }

    fun feParamGetActividades(){

    }
}