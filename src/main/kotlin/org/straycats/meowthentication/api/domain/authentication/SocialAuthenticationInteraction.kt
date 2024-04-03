package org.straycats.meowthentication.api.domain.authentication

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.authentication.provider.SocialClientProvider
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType
import org.straycats.meowthentication.api.domain.token.RefreshableToken

@Service
class SocialAuthenticationInteraction(
    private val socialClientProvider: SocialClientProvider
) {
    fun authentication(
        provider: SocialType,
        code: String,
        redirectedUrl: String? = null
    ): RefreshableToken {
        val authenticator = socialClientProvider.get(provider)
        return authenticator.authorize(code, redirectedUrl)
    }

    fun getProfile(provider: SocialType, accessToken: String): SocialProfile {
        val authenticator = socialClientProvider.get(provider)
        return authenticator.getProfile(accessToken)
    }
}
