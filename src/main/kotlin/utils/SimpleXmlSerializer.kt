package org.godker.utils

import org.simpleframework.xml.core.Persister
import java.io.StringWriter

class SimpleXmlSerializer {
    private val serializer = Persister()

    fun <T> toXml(obj: T): String {
        val writer = StringWriter()
        serializer.write(obj, writer)
        return writer.toString().trimIndent()
    }

    fun <T> fromXml(xml: String, clazz: Class<T>): T {
        return serializer.read(clazz, xml)
    }
}