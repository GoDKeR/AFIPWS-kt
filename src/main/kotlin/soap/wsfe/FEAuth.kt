package org.godker.soap.wsfe

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Auth")
data class FEAuth(
    @field:Element("Token")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val token: String,

    @field:Element("Sign")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val sign: String,

    @field:Element("Cuit")
    @field:Namespace(prefix = "ar", reference = "http://ar.gov.afip.dif.FEV1/")
    val Cuit: Long
)
