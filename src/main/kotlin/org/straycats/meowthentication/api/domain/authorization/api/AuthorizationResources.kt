package org.straycats.meowthentication.api.domain.authorization.api

import io.swagger.v3.oas.annotations.media.Schema
import org.straycats.meowthentication.api.domain.token.TokenType

class AuthorizationResources {

    class Request {
        @Schema(name = "Authorization.Request.CodeAuthorization")
        data class CodeAuthorization(
            val code: String,
            val scopes: List<String>,
            val issueType: TokenType,
            val attributes: Map<String, Any> = emptyMap(),
            val redirectUrl: String? = null
        ) {
            companion object
        }

        @Schema(name = "Authorization.Request.TokenAuthorization")
        data class TokenAuthorization(
            val accessToken: String,
            val scopes: List<String>,
            val issueType: TokenType,
            val attributes: Map<String, Any> = emptyMap()
        ) {
            companion object
        }
    }
}
