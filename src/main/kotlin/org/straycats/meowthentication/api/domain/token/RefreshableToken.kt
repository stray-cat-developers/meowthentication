package org.straycats.meowthentication.api.domain.token

data class RefreshableToken(
    val tokenType: String,
    val access: String,
    val accessExpiresInSeconds: Long,
    val refresh: String,
    val refreshExpiresInSeconds: Long,
)
