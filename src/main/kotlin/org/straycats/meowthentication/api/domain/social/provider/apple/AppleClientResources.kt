package org.straycats.meowthentication.api.domain.social.provider.apple

import com.fasterxml.jackson.annotation.JsonAlias

class AppleClientResources {
    class Reply {
        data class Token(
            @JsonAlias("access_token")
            val accessToken: String,
            @JsonAlias("token_type")
            val tokenType: String,
            @JsonAlias("expires_in")
            val expiresIn: Long,
            @JsonAlias("refresh_token")
            val refreshToken: String,
            @JsonAlias("id_token")
            val idToken: String
        )
    }
}
