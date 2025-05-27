package org.godker.soap

//Una limitación en SimpleXML no infiere correctamente las etiquetas al usar genéricos
//Por lo que la clase SoapEnvelope simplemente se construye un string

object SoapEnvelope{
    fun toXml(body: String): String {
        return """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
                <soapenv:Header/>
                <soapenv:Body>
                    $body
                </soapenv:Body>
            </soapenv:Envelope>
        """.trimIndent()
    }
}