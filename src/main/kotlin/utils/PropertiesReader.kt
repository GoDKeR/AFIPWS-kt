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
                    setProperty("WSAA_URL", "https://wsaa.afip.gov.ar/ws/services/LoginCms")
                    setProperty("WSAA_URL_HOMOLOGACION", "https://wsaahomo.afip.gov.ar/ws/services/LoginCms")
                    setProperty("WSFE_URL", "https://servicios1.afip.gov.ar/wsfev1/service.asmx")
                    setProperty("WSFE_URL_HOMOLOGACION", "https://wswhomo.afip.gov.ar/wsfev1/service.asmx")
                    setProperty("CUIT", "00000000000")
                    setProperty("CACHE_DIR", "cache/")
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