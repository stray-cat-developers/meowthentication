package org.straycats.meowthentication.api.domain.token

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.token.verifier.TokenVerifierProvider

@Service
class JsonWebTokenInteraction(
    private val tokenVerifierProvider: TokenVerifierProvider
) {

    private val tokenType = TokenType.JWT

    fun getClaims(token: String): Map<String, Any?> {
        val verifier = tokenVerifierProvider.get(tokenType)
        return verifier.getClaims(token)
    }
}
