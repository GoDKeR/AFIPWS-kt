package org.godker.soap.requests

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text


@Root(name = "Envelope")
@NamespaceList(
    Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "wsaa", reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
)
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginCmsRequest(
    @field:Element(name = "Header", required = false)
    val header: LoginCmsRequestHeader? = LoginCmsRequestHeader(),

    @field:Element(name = "Body")
    val body: LoginCmsRequestBody
)

@Root(name = "Header", strict = false)
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
class LoginCmsRequestHeader()

@Root(name = "Body")
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginCmsRequestBody(
    @field:Element(name = "loginCms")
    val loginCms: LoginCmsRequestData
)

@Root(name = "loginCms")
@Namespace(prefix = "wsaa", reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
data class LoginCmsRequestData(
    @field:Element(name = "in0")
    val cms: In0Wrapper
)

@Root(name = "in0")
@Namespace(prefix = "wsaa", reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
data class In0Wrapper (
    @field:Text
    val cms: String
)