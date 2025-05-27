package org.godker.soap.wsaa

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "loginTicketResponse")
data class LoginTicketResponse (
    @field:Attribute(name = "version", required = false)
    var version: String = "",

    @field:Element(name = "header")
    var header: LoginTicketResponseHeader = LoginTicketResponseHeader(),

    @field:Element(name = "credentials")
    var credentials: LoginTicketResponseCredentials = LoginTicketResponseCredentials()
)

data class LoginTicketResponseHeader(
    @field:Element(name = "source")
    var source: String = "",

    @field:Element(name = "destination")
    var destination: String = "",

    @field:Element(name = "uniqueId")
    var uniqueId: String = "",

    @field:Element(name = "generationTime")
    var generationTime: String = "",

    @field:Element(name = "expirationTime")
    var expirationTime: String = ""
)

data class LoginTicketResponseCredentials(
    @field:Element(name = "token")
    var token: String = "",

    @field:Element(name = "sign")
    var sign: String = "",
)