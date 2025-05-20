package org.godker.utils

import java.io.File
import java.util.Properties

object PropertiesReader {
    private val properties = Properties()

    init {

        try {
            val file = File("config.properties")

            if (!file.exists()){
                properties.apply {
                    setProperty("CERT", "default_cert.crt")
                    setProperty("KEY", "default_key.key")
                }

                file.outputStream().use { properties.store(it, "Generado automáticamente. Completar") }
            }else{
                file.inputStream().use { properties.load(it) }
            }

        } catch (e: Exception) {
            //TODO
        }
    }

    fun getProperty(key: String): String {
        return properties.getProperty(key) ?: run {
            properties.setProperty(key, "")
            ""
        }
    }
}