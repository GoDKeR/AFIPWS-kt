package org.godker.utils

import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder
import org.bouncycastle.cms.CMSProcessableByteArray
import org.bouncycastle.cms.CMSSignedDataGenerator
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.openssl.PEMKeyPair
import org.bouncycastle.openssl.PEMParser
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder
import java.io.File
import java.security.PrivateKey
import java.security.Security
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate

class CryptoService (private val certificatePath: String,
                     private val privateKeyPath: String) {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    private fun loadCertKey(): Pair<X509Certificate, PrivateKey> {

        try {
            //Se carga el certificado
            val factory = CertificateFactory.getInstance("X.509")
            val cert =
                factory.generateCertificate(File(certificatePath).inputStream()) as X509Certificate

            //Se carga la key
            val parser = PEMParser(File(privateKeyPath).reader())
            val converter = JcaPEMKeyConverter().setProvider("BC")
            val keyObject = parser.readObject()
            val key = converter.getKeyPair(keyObject as PEMKeyPair).private

            return cert to key
        }catch (cce: ClassCastException){
            println("##ERROR: Revisar que el certificado y la pk esten en el formato correcto.")
            throw ClassCastException()
        }
    }

    fun sign(xml: String) : ByteArray{

        val (cert, private) = loadCertKey()

        val holder = JcaX509CertificateHolder(cert)
        val signer = JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(private)

        val signerInfoGen = JcaSignerInfoGeneratorBuilder(
            JcaDigestCalculatorProviderBuilder().setProvider("BC").build()
        )
            .build(signer, cert)

        val generator = CMSSignedDataGenerator()
        generator.addSignerInfoGenerator(signerInfoGen)
        generator.addCertificates { listOf(holder) }

        val cmsData = CMSProcessableByteArray(xml.toByteArray())
        val signedData = generator.generate(cmsData, true)

        return signedData.encoded
    }
}