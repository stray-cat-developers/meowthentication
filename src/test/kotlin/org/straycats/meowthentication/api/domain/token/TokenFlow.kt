package org.straycats.meowthentication.api.domain.token

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.config.AuthorizationHeader
import org.straycats.meowthentication.api.domain.token.api.JsonWebTokenController
import org.straycats.meowthentication.utils.fromJson

class TokenFlow(
    private val mockMvc: MockMvc
) {

    fun getClaims(token: String): Map<String, Any?> {
        val authorization = AuthorizationHeader("Bearer", token)
        val uri = linkTo<JsonWebTokenController> { getClaims(authorization) }
            .toUri()

        return mockMvc.get(uri) {
            contentType = MediaType.APPLICATION_JSON
            header(HttpHeaders.AUTHORIZATION, authorization.toHeaderString())
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<Map<String, Any?>>>()
            .content!!
    }
}
