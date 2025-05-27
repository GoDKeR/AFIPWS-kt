package org.godker.soap.wsfe

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "FECAESolicitar")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class FECAESolicitar(
    @field:Element(name = "Auth", required = true)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val auth: FEAuth,

    @field:Element(name = "FeCAEReq", required = true)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val request: FECAEReq
)

@Root(name = "FeCAEReq")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class FECAEReq(
    @field:Element(name = "FeCabReq")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val cabecera: FECAECabReq,

    @field:Element(name = "FeDetReq")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val detalle: FECAEDetReq
)

data class FECAECabReq(
    @field:Element(name = "CantReg")
    val cant: Int,

    @field:Element(name = "PtoVta")
    val ptovta: Int,

    @field:Element(name = "CbteTipo")
    val tipo: Int
)

data class FECAEDetReq(
    @field:ElementList(name = "FECAEDetRequest", inline = true)
    val caeDetReqs: MutableList<FeCAEDetItem>
)

@Root(name = "FECAEDetRequest")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class FeCAEDetItem(
    @field:Element("Concepto")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val concepto: Int,

    @field:Element("DocTipo")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val docTipo: Int,

    @field:Element("DocNro")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val docNro: Long,

    @field:Element("CbteDesde")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val cbteDesde: Long,

    @field:Element("CbteHasta")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val cbteHasta: Long,

    @field:Element("CbteFch", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val cbteFch: String,

    @field:Element("ImpTotal")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impTotal: Double,

    @field:Element("ImpTotConc")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impTotConc: Double,

    @field:Element("ImpNeto")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impNeto: Double,

    @field:Element("ImpOpEx")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impOpEx: Double,

    @field:Element("ImpTrib")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impTrib: Double,

    @field:Element("ImpIVA")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val impIVA: Double,

    @field:Element("FchServDesde", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val fchServDesde: String,

    @field:Element("FchServHasta", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val fchServHasta: String,

    @field:Element("FchVtoPago", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val fchVtoPago: String,

    @field:Element("MonId")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val monId: String,

    @field:Element("MonCotiz", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val monCotiz: Double,

    @field:Element("CanMisMonExt", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val canMisMonExt: String,

    @field:Element("CondicionIVAReceptorId", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val condicionIVAReceptorId: Int,

    @field:Element(name="CbtesAsoc", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val cbtesAsoc: CbteAsocWrapper,

    @field:Element(name = "Tributos", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val tributos: TributoWrapper? = null,

    @field:Element(name = "Iva", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val IVA: AlicIvaWrapper? = null,

    @field:Element(name = "Opcionales", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val opcionales: OpcionalWrapper? = null,

    @field:Element(name = "Compradores", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val compradores: CompradorWrapper? = null,

    @field:Element(name = "PeriodoAsoc", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val periodoAsoc: PeriodoAsoc? = null,

    @field:Element(name = "Actividades", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val actividades: ActividadWrapper? = null,
)

@Root(name = "CbtesAsoc")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class CbteAsocWrapper(
    @field:ElementList(name = "CbteAsoc", inline = true)
    var cbtesAsoc: List<ComprobanteAsociado> = ArrayList()
)

@Root(name = "CbteAsoc")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class ComprobanteAsociado(
    @field:Element("Tipo")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    var tipo: Int = 1,

    @field:Element("PtoVta")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    var ptoVta: Int=0,

    @field:Element("Nro")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    var nro: Int = 0,

    @field:Element("Cuit", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    var cuit: String = "",

    @field:Element("CbteFch", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    var fecha: String = ""
)

@Root(name = "Tributos")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class TributoWrapper(
    @field:ElementList(name = "Tributo", inline = true)
    var tributos: List<Tributo> = ArrayList()
)

@Root(name = "Tributo")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class Tributo(
    @field:Element("Nro")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val id: Int,

    @field:Element("Desc", required = false)
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val desc: String = "",

    @field:Element("BaseImp")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val baseImp: Double,

    @field:Element("Alic")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val alic: Double,

    @field:Element("Importe")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val importe: Double
)

@Root(name = "Iva")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class AlicIvaWrapper(
    @field:ElementList(name = "AlicIva", inline = true)
    var alicIva: List<AlicIVA> = ArrayList()
)
@Root(name = "AlicIva")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class AlicIVA(
    @field:Element("Id")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val id: Int,

    @field:Element("BasImp")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val baseImp: Double,

    @field:Element("Importe")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val importe: Double
)

@Root(name = "Opcionales")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class OpcionalWrapper(
    @field:ElementList(name = "Opcional", inline = true)
    var opcional: List<Opcional> = ArrayList()
)

@Root(name = "Opcional")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class Opcional(
    @field:Element("Id")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val id: Int,

    @field:Element("Valor")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val valor: String
)

@Root(name = "Compradores")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class CompradorWrapper(
    @field:ElementList(name = "Comprador", inline = true)
    var comprador: List<Comprador> = ArrayList()
)

@Root(name = "Comprador")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class Comprador(
    @field:Element("DocTip")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val docTipo: Int,

    @field:Element("DocNro")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val docNro: String,

    @field:Element("Porcentaje")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val porcentaje: Double
)

@Root(name = "PeriodoAsoc")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class PeriodoAsoc(
    @field:Element("FchDesde")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val fchDesde: String,

    @field:Element("FchHasta")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val fchHasta: String
)

@Root(name = "Actividades")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class ActividadWrapper(
    @field:ElementList(name = "Actividad", inline = true)
    var actividad: List<Actividad> = ArrayList()
)

@Root(name = "Actividad")
@Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
data class Actividad(
    @field:Element("Id")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val id: Long
)
