package org.godker.soap.wsaa

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.NamespaceList
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text


@Root(name = "Envelope")
@NamespaceList(
    Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/"),
    Namespace(prefix = "xsd", reference = "http://www.w3.org/2001/XMLSchema"),
    Namespace(prefix = "xsi", reference = "http://www.w3.org/2001/XMLSchema-instance")
)
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginCmsResponse(

    @field:Element(name = "Body", required = false)
    var body: LoginCmsResponseBody? = null
)

@Root(name = "Body")
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginCmsResponseBody(
    @field:Element(name = "Fault", required = false)
    var fault: LoginCmsFault? = null,

    @field:Element(name = "loginCmsResponse", required = false)
    var loginCmsResponse: LoginCmsResponseData? = null
)

@Root(name = "Fault", strict = false)
@Namespace(prefix = "soapenv", reference = "http://schemas.xmlsoap.org/soap/envelope/")
data class LoginCmsFault(
    @field:Element(name = "faultcode")
    @field:Namespace("http://xml.apache.org/axis/", "ns1")
    var faultCode: String = "",

    @field:Element(name = "faultstring")
    var faultString: String = "",

    @field:Element(name = "detail", required = false)
    var detail: LoginCmsFaultDetail? = null
)

@Root(name = "detail", strict = false)
data class LoginCmsFaultDetail(
    @field:Element(name = "exceptionName", required = false)
    @field:Namespace(prefix = "ns2", reference = "http://xml.apache.org/axis/")
    var exceptionName: String? = null,

    @field:Element(name = "hostname", required = false)
    @field:Namespace(prefix = "ns3", reference = "http://xml.apache.org/axis/")
    var hostname: String? = null
)

@Root(name = "loginCmsResponse", strict = false)
@Namespace(reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
data class LoginCmsResponseData(
    @field:Element(name = "loginCmsReturn")
    var data: LoginCmsReturn? = null
)

@Root(name = "loginCmsReturn", strict = false)
data class LoginCmsReturn (
    @field:Text
    var data: String = ""
)