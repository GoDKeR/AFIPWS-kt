package org.godker.soap

import org.godker.afip.Factura
import org.godker.afip.attachToXml
import org.godker.utils.xmlToMap
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringWriter
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class SoapEnvelope {

    private val registeredNamespaces = mutableMapOf<String, String>()
    private val body: Map<String, Any?>?
    private val head: Map<String, Any?>?

    constructor(body: Map<String, Any?>, head: Map<String, Any?>? = null){
        this.head = head
        this.body = body
    }

    constructor(xml: String){
        val parsed = xml.xmlToMap(true) ?: throw IllegalArgumentException("XML Inválido.")

        body = parsed["Body"] as? Map<String, Any?> ?: throw IllegalArgumentException("No se encontró el Body.")
        head = parsed["Header"] as? Map<String, Any?> ?: emptyMap()

        /*
        Not sure if ill need to register the namespaces in this, so: TODO
         */
    }

    operator fun get(index: String): Any?{
        return body?.get(index)
    }

    fun registerNamespace(prefix: String, uri: String) {
        if (!registeredNamespaces.containsKey(prefix))
            registeredNamespaces[prefix] = uri
    }

    fun build(): String{
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = docBuilder.newDocument()

        val envelope = document.createElement("soapenv:Envelope")
        envelope.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/")

        registeredNamespaces.forEach { (prefix, uri) ->
            envelope.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:$prefix", uri)
        }

        val soapBody = document.createElement("soapenv:Body")

        if (body != null) {
            for ((key, value) in body) {
                val child = document.createElement(key)

                if (value is Map<*, *>) {
                    //value *should* always be String, Any?, but safely cast it just in case
                    mapToElement(document, child, value as? Map<String, Any?> ?: continue)
                } else if (value is Factura) {
                    value.attachToXml(child, document)
                } else {
                    child.textContent = value.toString()
                }

                soapBody.appendChild(child)
            }
        }

        envelope.appendChild(soapBody)
        document.appendChild(envelope)

        val transformer = TransformerFactory.newInstance().newTransformer().apply {
            setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no")
            setOutputProperty(OutputKeys.INDENT, "yes")
        }

        val writer = StringWriter()
        transformer.transform(DOMSource(document), StreamResult(writer))
        return writer.toString()
    }

    private fun mapToElement(document: Document, parent: Element, map: Map<String, Any?>){

        for((key, value) in map){
            val child = document.createElement(key)

            if (value is Map<*, *>){
                mapToElement(document, child, value as? Map<String, Any?> ?: continue)
            }else if (value is Factura){
                value.attachToXml(child, document)
            }else{
                child.textContent = value.toString()
            }

            parent.appendChild(child)
        }
    }
}