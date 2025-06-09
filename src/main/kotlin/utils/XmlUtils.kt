package org.godker.utils

import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.lang.Exception
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.io.reader

fun Map<String, Any?>.getMap(key: String): Map<String, Any?>? = this[key] as? Map<String, Any?>

fun String.xmlToMap(stripNamespaces: Boolean = false): Map<String, Any?>? {

    try {
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        val document = docBuilder.parse(InputSource(this.reader()))
        val root = document.documentElement

        return elementToMap(root, stripNamespaces)
    } catch (e: Exception) {
        println(e.message)
    }

    return null
}

private fun elementToMap(element: Element, stripNamespaces: Boolean = false): Map<String, Any?> {
    val map = mutableMapOf<String, Any?>()

    val childs = element.childNodes

    for (i in 0..<childs.length) {
        val child = childs.item(i)

        if (child is Element) {
            val key = if (stripNamespaces) child.nodeName.substringAfter(":") else child.nodeName
            val value = if (child.hasChildNodes()) {

                val first = child.firstChild
                if (child.childNodes.length == 1 && first.nodeType == Node.TEXT_NODE) {
                    first.nodeValue
                } else {
                    elementToMap(child, stripNamespaces)
                }
            } else {
                null
            }

            if (map.containsKey(key)) {
                val existing = map[key]
                if (existing is MutableList<*>) {
                    (existing as MutableList<Any?>).add(value)
                } else {
                    map[key] = mutableListOf(existing, value)
                }
            } else {
                map[key] = value
            }
        }
    }

    return map
}