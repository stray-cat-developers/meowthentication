package org.straycats.meowthentication.api.domain.token.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.config.AuthorizationHeader
import org.straycats.meowthentication.api.domain.token.JsonWebTokenInteraction
import org.straycats.meowthentication.utils.toReply

@RestController
@RequestMapping("v1/token/jwt")
class JsonWebTokenController(
    private val jsonWebTokenInteraction: JsonWebTokenInteraction
) {
    @Operation(description = "Verifying JWT and decode claims")
    @GetMapping("claims")
    fun getClaims(
        @RequestHeader(HttpHeaders.AUTHORIZATION) authorization: AuthorizationHeader
    ): Reply<Map<String, Any?>> {
        return jsonWebTokenInteraction.getClaims(authorization.token).toReply()
    }
}
