package org.straycats.meowthentication.api.domain.token.issuer

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.token.TokenType

@Service
class TokenIssuerProvider(
    private val environment: AppEnvironment
) {

    fun get(tokenType: TokenType): TokenIssuer {
        return when (tokenType) {
            TokenType.JWT -> JsonWebTokenIssuer(environment.token.jwt)
        }
    }
}
