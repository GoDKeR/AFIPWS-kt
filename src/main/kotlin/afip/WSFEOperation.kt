package org.godker.afip

enum class WSFEOperation(val action: String) {
    FECAESolicitar("FECAESolicitar"),
    FECAEASolicitar("FECAEASolicitar"),
    FECAEAConsultar ("FECAEAConsultar "),
    FEParamGetTiposCbte("FEParamGetTiposCbte"),
    FEParamGetTiposConcepto("FEParamGetTiposConcepto"),
    FEParamGetTiposDoc("FEParamGetTiposDoc"),
    FEParamGetTiposIVA("FEParamGetTiposIVA"),
    FEParamGetTiposMoneda("FEParamGetTiposMoneda"),
    FEParamGetTiposOpcional("FEParamGetTiposOpcional"),
    FEParamGetTiposTributos("FEParamGetTiposTributos"),
    FEParamGetPtosVenta("FEParamGetPtosVenta"),
    FEParamGetCotizacion("FEParamGetCotizacion"),
    FEDummy("FEDummy"),
    FECompUltimoAutorizado("FECompUltimoAutorizado"),
    FECompTotXRequest("FECompTotXRequest"),
    FECompConsultar("FECompConsultar"),
    FEParamGetActividades("FEParamGetActividades")
}