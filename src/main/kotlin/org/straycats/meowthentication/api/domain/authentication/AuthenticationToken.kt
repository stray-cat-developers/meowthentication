package org.straycats.meowthentication.api.domain.authentication

data class AuthenticationToken(
    val tokenType: String,
    val access: String,
    val accessExpiresInSeconds: Long,
    val refresh: String,
    val refreshExpiresInSeconds: Long,
)
