package org.godker.soap.requests

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "loginTicketRequest")
data class LoginTicketRequest(
    @field:Element(name = "header")
    val header: LoginTicketRequestHeader,

    @field:Element(name = "service")
    val service: String
)

data class LoginTicketRequestHeader(
    @field:Element(name = "uniqueId")
    val uniqueId: String,

    @field:Element(name = "generationTime")
    val generationTime: String,

    @field:Element(name = "expirationTime")
    val expirationTime: String
)
