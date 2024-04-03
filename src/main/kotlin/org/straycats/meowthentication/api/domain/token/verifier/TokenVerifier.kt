package org.straycats.meowthentication.api.domain.token.verifier

interface TokenVerifier {
    fun verify(token: String): Boolean
    fun verifyOrThrow(token: String)
    fun getClaims(token: String): Map<String, Any?>
}
