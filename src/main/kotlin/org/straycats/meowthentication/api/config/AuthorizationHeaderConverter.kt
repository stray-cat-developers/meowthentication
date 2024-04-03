package org.straycats.meowthentication.api.config

import org.springframework.core.convert.converter.Converter

class AuthorizationHeaderConverter : Converter<String, AuthorizationHeader> {

    override fun convert(source: String): AuthorizationHeader? {
        if (source.isEmpty())
            return null
        val blocks = source.split(" ").map { it.trim() }
        return AuthorizationHeader(
            blocks.first(),
            blocks.last()
        )
    }
}

data class AuthorizationHeader(
    val type: String,
    val token: String
) {
    fun toHeaderString() = "$type $token"
}
