package org.godker.soap

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class SoapClient (private val client: OkHttpClient) {

    private val mediaType = "text/xml; charset=utf-8".toMediaType()

    fun sendSoapRequest(endpoint: String, xml: String): String {
        val body = xml.trim().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(endpoint)
            .post(body)
            .header("Content-Type", mediaType.toString())
            .header("SOAPAction", "\"\"")
            .header("Content-Length", body.contentLength().toString())
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println(response.body!!.string())
                throw RuntimeException("Error al enviar el request. HTTP: ${response.code}. ${response.message}")
            }
            return response.body?.string() ?: throw RuntimeException("Respuesta HTTP vacía.")
        }
    }
}