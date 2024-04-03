package org.straycats.meowthentication.api.domain.authentication

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.authentication.provider.AuthenticationProviderHandler
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType

@Service
class SocialAuthenticationInteraction(
    private val authenticationProviderHandler: AuthenticationProviderHandler
) {
    fun authentication(
        provider: SocialType,
        code: String,
        redirectedUrl: String? = null
    ): AuthenticationToken {
        val authenticator = authenticationProviderHandler.getSocialClient(provider)
        return authenticator.authorize(code, redirectedUrl)
    }

    fun getProfile(provider: SocialType, accessToken: String): SocialProfile {
        val authenticator = authenticationProviderHandler.getSocialClient(provider)
        return authenticator.getProfile(accessToken)
    }
}
