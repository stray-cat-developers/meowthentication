package org.straycats.meowthentication.api.domain.token.verifier

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.token.JsonWebToken
import org.straycats.meowthentication.api.domain.token.TokenType

@Service
class TokenVerifierProvider(
    private val environment: AppEnvironment
) {

    fun get(tokenType: TokenType): TokenVerifier {
        return when (tokenType) {
            TokenType.JWT -> JsonWebToken(environment.token.jwt)
        }
    }
}
