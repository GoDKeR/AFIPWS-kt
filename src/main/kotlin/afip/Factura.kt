package org.godker.afip

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

data class Factura(
    val concepto: Int,
    val docTipo: Int,
    val docNro: Long,
    val cbteDesde: Long,
    val cbteHasta: Long,
    val cbteFch: String? = null,
    val impTotal: Double,
    val impTotConc: Double,
    val impNeto: Double,
    val impOpEx: Double,
    val impTrib: Double,
    val impIVA: Double,
    val fchServDesde: String? = null,
    val fchServHasta: String? = null,
    val fchVtoPago: String? = null,
    val monId: String,
    val monCotiz: Double? = null,
    val canMisMonExt: String? = null,
    val condicionIVAReceptorId: Int? = null,
    val cbtesAsoc: ArrayList<ComprobanteAsociado>? = null,
    val tributos: ArrayList<Tributo>? = null,
    val iva: ArrayList<AlicIva>? = null,
    val opcionales: ArrayList<Opcional>? = null,
    val compradores: ArrayList<Comprador>? = null,
    val periodoAsoc: Periodo? = null,
    val actividades: ArrayList<Actividad>? = null
)

data class ComprobanteAsociado(
    val tipo: Int,
    val ptoVta: Int,
    val nro: Long,
    val cuit: String? = null,
    val cbteFch: String? = null
)

data class Tributo(
    val id: Int,
    val desc: String? = null,
    val baseImp: Double,
    val alic: Double,
    val importe: Double
)

data class AlicIva(
    val id: Int,
    val baseImp: Double,
    val importe: Double
)

data class Opcional(
    val id: Int,
    val valor: String
)

data class Comprador(
    val docTipo: Int,
    val docNro: String,
    val porcentaje: Double
)

data class Periodo(
    val fchDesde : String,
    val fchHasta: String
)

data class Actividad(
    val id: Int
)

//Helper extension function to serialize into XMLElement
fun Factura.attachToXml(parent: Element, document: Document) {

    val root = document.createElement("ar:FECAEDetRequest")
    parent.appendChild(root)

    fun Element.append(tag: String, value: Any?) {
        if (value != null) {
            val elem = document.createElement(tag)
            elem.textContent = value.toString()
            appendChild(elem)
        }
    }

    root.apply {
        append("ar:Concepto", concepto)
        append("ar:DocTipo", docTipo)
        append("ar:DocNro", docNro)
        append("ar:CbteDesde", cbteDesde)
        append("ar:CbteHasta", cbteHasta)
        append("ar:CbteFch", cbteFch)
        append("ar:ImpTotal", impTotal)
        append("ar:ImpTotConc", impTotConc)
        append("ar:ImpNeto", impNeto)
        append("ar:ImpOpEx", impOpEx)
        append("ar:ImpTrib", impTrib)
        append("ar:ImpIVA", impIVA)
        append("ar:FchServDesde", fchServDesde)
        append("ar:FchServHasta", fchServHasta)
        append("ar:FchVtoPago", fchVtoPago)
        append("ar:MonId", monId)
        append("ar:MonCotiz", monCotiz)
        append("ar:CanMisMonExt", canMisMonExt)
        append("ar:CondicionIVAReceptorId", condicionIVAReceptorId)

        cbtesAsoc?.takeIf { it.isNotEmpty() }?.let {
            val cbtesElem = document.createElement("ar:CbtesAsoc")
            it.forEach { cbte ->
                val cbteElem = document.createElement("ar:CbteAsoc")
                cbteElem.append("ar:Tipo", cbte.tipo)
                cbteElem.append("ar:PtoVta", cbte.ptoVta)
                cbteElem.append("ar:Nro", cbte.nro)
                cbteElem.append("ar:Cuit", cbte.cuit)
                cbteElem.append("ar:CbteFch", cbte.cbteFch)
                cbtesElem.appendChild(cbteElem)
            }
            appendChild(cbtesElem)
        }

        tributos?.takeIf { it.isNotEmpty() }?.let {
            val tributosElem = document.createElement("ar:Tributos")
            it.forEach { tributo ->
                val tribElem = document.createElement("ar:Tributo")
                tribElem.append("ar:Id", tributo.id)
                tribElem.append("ar:Desc", tributo.desc)
                tribElem.append("ar:BaseImp", tributo.baseImp)
                tribElem.append("ar:Alic", tributo.alic)
                tribElem.append("ar:Importe", tributo.importe)
                tributosElem.appendChild(tribElem)
            }
            appendChild(tributosElem)
        }

        iva?.takeIf { it.isNotEmpty() }?.let {
            val ivaElem = document.createElement("ar:Iva")
            it.forEach { alic ->
                val aElem = document.createElement("ar:AlicIva")
                aElem.append("ar:Id", alic.id)
                aElem.append("ar:BaseImp", alic.baseImp)
                aElem.append("ar:Importe", alic.importe)
                ivaElem.appendChild(aElem)
            }
            appendChild(ivaElem)
        }

        opcionales?.takeIf { it.isNotEmpty() }?.let {
            val opcElem = document.createElement("ar:Opcionales")
            it.forEach { opc ->
                val oElem = document.createElement("ar:Opcional")
                oElem.append("ar:Id", opc.id)
                oElem.append("ar:Valor", opc.valor)
                opcElem.appendChild(oElem)
            }
            appendChild(opcElem)
        }

        compradores?.takeIf { it.isNotEmpty() }?.let {
            val compradoresElem = document.createElement("ar:Compradores")
            it.forEach { comp ->
                val cElem = document.createElement("ar:Comprador")
                cElem.append("ar:DocTipo", comp.docTipo)
                cElem.append("ar:DocNro", comp.docNro)
                cElem.append("ar:Porcentaje", comp.porcentaje)
                compradoresElem.appendChild(cElem)
            }
            appendChild(compradoresElem)
        }

        periodoAsoc?.let {
            val periodoElem = document.createElement("ar:PeriodoAsoc")
            periodoElem.append("ar:FchDesde", it.fchDesde)
            periodoElem.append("ar:FchHasta", it.fchHasta)
            appendChild(periodoElem)
        }

        actividades?.takeIf { it.isNotEmpty() }?.let {
            val actsElem = document.createElement("ar:Actividades")
            it.forEach { act ->
                val actElem = document.createElement("ar:Actividad")
                actElem.append("ar:Id", act.id)
                actsElem.appendChild(actElem)
            }
            appendChild(actsElem)
        }
    }
}
