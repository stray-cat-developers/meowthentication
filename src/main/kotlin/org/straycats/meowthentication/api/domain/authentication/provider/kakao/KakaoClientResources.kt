package org.straycats.meowthentication.api.domain.authentication.provider.kakao

import com.fasterxml.jackson.annotation.JsonAlias
import org.straycats.meowthentication.utils.RestClientSupport

class KakaoClientResources {

    class Reply {
        data class Token(
            @JsonAlias("token_type")
            val tokenType: String,
            @JsonAlias("access_token")
            val accessToken: String,
            @JsonAlias("refresh_token")
            val refreshToken: String,
            @JsonAlias("expires_in")
            val expiresIn: Long,
            @JsonAlias("refresh_token_expires_in")
            val refreshTokenExpiresIn: Long
        )

        data class Profile(
            val id: String,
            @JsonAlias("kakao_account")
            val kakaoAccount: KakaoAccount?
        ) {
            data class KakaoAccount(
                val name: String?,
                val email: String?
            )
        }

        data class AuthenticationError(
            @JsonAlias("error")
            val error: String,
            @JsonAlias("error_code")
            val errorCode: String,
            @JsonAlias("error_description")
            val errorDescription: String,
        ) : RestClientSupport.ExternalServiceError(
            errorCode,
            errorDescription
        )
    }
}
