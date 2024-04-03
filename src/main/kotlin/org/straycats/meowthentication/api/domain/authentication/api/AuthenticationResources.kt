package org.straycats.meowthentication.api.domain.authentication.api

import io.swagger.v3.oas.annotations.media.Schema
import org.straycats.meowthentication.api.domain.authentication.AuthenticationToken
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType

class AuthenticationResources {

    class Request {
        @Schema(name = "Authentication.Request.CodeAuthentication")
        data class CodeAuthentication(
            val code: String,
            val redirectedUrl: String?
        ) {
            companion object
        }

        @Schema(name = "Authentication.Request.TokenAuthentication")
        data class TokenAuthentication(
            val accessToken: String,
        ) {
            companion object
        }
    }

    class Reply {
        @Schema(name = "Authentication.Reply.SocialAuthentication")
        data class SocialAuthentication(
            @Schema(description = "Social Type")
            val socialType: SocialType,
            @Schema(description = "Social ID")
            val socialIdentifyKey: String,
            @Schema(description = "Email registered in social media service")
            val socialEmail: String?
        ) {
            companion object {
                fun from(
                    socialType: SocialType,
                    token: AuthenticationToken,
                    profile: SocialProfile
                ): SocialAuthentication {
                    return token.run {
                        SocialAuthentication(
                            socialType,
                            profile.providerUserId,
                            profile.providerUserEmail
                        )
                    }
                }

                fun from(
                    socialType: SocialType,
                    profile: SocialProfile
                ): SocialAuthentication {
                    return SocialAuthentication(
                        socialType,
                        profile.providerUserId,
                        profile.providerUserEmail
                    )
                }
            }
        }
    }
}
