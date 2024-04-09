package org.straycats.meowthentication.api.domain.social

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.social.provider.SocialClientProvider
import org.straycats.meowthentication.api.domain.social.provider.SocialType

@Service
class SocialInteraction(
    private val socialClientProvider: SocialClientProvider
) {
    fun getProfileByCode(provider: SocialType, code: String, redirectedUrl: String? = null): SocialProfile {
        val authenticator = socialClientProvider.get(provider)
        val token = authenticator.authorize(code, redirectedUrl).access
        return getProfileByToken(provider, token)
    }

    fun getProfileByToken(provider: SocialType, accessToken: String): SocialProfile {
        val authenticator = socialClientProvider.get(provider)
        return authenticator.getProfile(accessToken)
    }
}
