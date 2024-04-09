package org.straycats.meowthentication.api.domain.social.provider.naver

import com.fasterxml.jackson.annotation.JsonAlias
import org.straycats.meowthentication.utils.RestClientSupport

class NaverClientResources {

    class Reply {
        data class Token(
            @JsonAlias("token_type")
            val tokenType: String,
            @JsonAlias("access_token")
            val accessToken: String,
            @JsonAlias("refresh_token")
            val refreshToken: String,
            @JsonAlias("expires_in")
            val expiresInSeconds: Long
        )

        data class Profile(
            @JsonAlias("resultcode")
            val resultCode: String,
            val message: String,
            @JsonAlias("response")
            val naver: NaverProfile?
        ) {
            data class NaverProfile(
                val id: String,
                val email: String
            )
        }

        data class Error(
            val error: String,
            @JsonAlias("error_description")
            val errorDescription: String
        ) : RestClientSupport.ExternalServiceError(
            error,
            errorDescription
        )
    }
}
