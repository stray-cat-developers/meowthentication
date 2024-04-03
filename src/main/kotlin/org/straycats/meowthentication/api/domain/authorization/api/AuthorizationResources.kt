package org.straycats.meowthentication.api.domain.authorization.api

import io.swagger.v3.oas.annotations.media.Schema
import org.straycats.meowthentication.api.domain.token.TokenType

class AuthorizationResources {

    class Request {
        @Schema(name = "Authorization.Request.CodeAuthentication")
        data class CodeAuthentication(
            val code: String,
            val scopes: List<String>,
            val issueType: TokenType,
            val attributes: Map<String, Any> = emptyMap()
        ) {
            companion object
        }

        @Schema(name = "Authorization.Request.TokenAuthentication")
        data class TokenAuthentication(
            val accessToken: String,
            val scopes: List<String>,
            val issueType: TokenType,
            val attributes: Map<String, Any> = emptyMap()
        ) {
            companion object
        }
    }
}
