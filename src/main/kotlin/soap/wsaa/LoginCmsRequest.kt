package org.godker.soap.wsaa

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import org.simpleframework.xml.Text


@Root(name = "loginCms")
@Namespace(prefix = "wsaa", reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
data class LoginCmsRequest(
    @field:Element(name = "in0")
    val cms: In0Wrapper
)

@Root(name = "in0")
@Namespace(prefix = "wsaa", reference = "http://wsaa.view.sua.dvadac.desein.afip.gov")
data class In0Wrapper (
    @field:Text
    val cms: String
)