package org.godker

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import okhttp3.OkHttpClient
import org.godker.afip.*
import org.godker.utils.CryptoService
import org.godker.utils.PropertiesReader
import org.godker.soap.SoapClient
import org.godker.soap.SoapEnvelope

import java.util.concurrent.TimeUnit

/*
    println(PropertiesReader.getProperty("CERT"))

    val serializer = SimpleXmlSerializer()

    val crypto = CryptoService(
        PropertiesReader.getProperty("CERT"),
        PropertiesReader.getProperty("KEY")
    )

    val httpClient = OkHttpClient()
    val soapClient = SoapClient(PropertiesReader.getProperty("URL"), httpClient)

    val wsaa = WSAA(AFIPWS.WSFEv1, crypto, serializer, soapClient)
    val tra = wsaa.llamarWSAA()

    println(tra)
 */

class Cli() : CliktCommand() {
    override fun help(context: Context): String = """
    Cliente para acceder a los Webservices de AFIP.    
    """.trimIndent()

    override fun run() {
        //TODO("Not yet implemented")

        val crypto = CryptoService(
            PropertiesReader.getProperty("CERT"),
            PropertiesReader.getProperty("KEY")
        )

        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val soapClient = SoapClient(httpClient)

        val wsaa = WSAA(AFIPServices.WSFEv1, crypto, soapClient)

        val credentials = wsaa.llamarWSAA()

        val wsfe = WSFEv1(credentials, soapClient)

        val factura = Factura(
            concepto = 1,
            docTipo = 80, // CUIT
            docNro = 20123456789,
            cbteDesde = 1,
            cbteHasta = 1,
            cbteFch = "20250603",
            impTotal = 121.0,
            impTotConc = 0.0,
            impNeto = 100.0,
            impOpEx = 0.0,
            impTrib = 0.0,
            impIVA = 21.0,
            fchServDesde = null,
            fchServHasta = null,
            fchVtoPago = null,
            monId = "PES",
            monCotiz = 1.0,
            canMisMonExt = null,
            condicionIVAReceptorId = 1,
            cbtesAsoc = arrayListOf(
                ComprobanteAsociado(
                    tipo = 6,
                    ptoVta = 1,
                    nro = 12345678,
                    cuit = "20304050607",
                    cbteFch = "20250501"
                )
            ),
            tributos = arrayListOf(
                Tributo(
                    id = 99,
                    desc = "Impuesto Interno",
                    baseImp = 100.0,
                    alic = 10.0,
                    importe = 10.0
                )
            ),
            iva = arrayListOf(
                AlicIva(
                    id = 5, // 21%
                    baseImp = 100.0,
                    importe = 21.0
                )
            ),
            opcionales = arrayListOf(
                Opcional(
                    id = 2101,
                    valor = "ValorOpcional"
                )
            ),
            compradores = arrayListOf(
                Comprador(
                    docTipo = 80,
                    docNro = "20999999999",
                    porcentaje = 100.0
                )
            ),
            periodoAsoc = Periodo(
                fchDesde = "20250401",
                fchHasta = "20250430"
            ),
            actividades = arrayListOf(
                Actividad(id = 123456)
            )
        )

        wsfe.feCAESolicitar(factura)
    }
}

fun main(args: Array<String>) = Cli().main(args)