package org.straycats.meowthentication.api.domain.authorization

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.social.SocialInteraction
import org.straycats.meowthentication.api.domain.social.SocialProfile
import org.straycats.meowthentication.api.domain.social.provider.SocialType
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.api.domain.token.TokenType
import org.straycats.meowthentication.api.domain.token.issuer.TokenIssuerProvider

@Service
class SocialAuthorizationInteraction(
    private val socialInteraction: SocialInteraction,
    private val tokenIssuerProvider: TokenIssuerProvider
) {

    fun authorizeWithCode(
        socialType: SocialType,
        code: String,
        tokenType: TokenType,
        scopes: List<String>,
        attributes: Map<String, Any>,
        redirectUrl: String? = null,
    ): RefreshableToken {
        val socialProfile = socialInteraction.getProfileByCode(socialType, code, redirectUrl)

        return authorize(socialType, socialProfile, tokenType, scopes, attributes)
    }

    fun authorizeWithToken(
        socialType: SocialType,
        accessToken: String,
        tokenType: TokenType,
        scopes: List<String>,
        attributes: Map<String, Any>
    ): RefreshableToken {
        val socialProfile = socialInteraction.getProfileByCode(socialType, accessToken)
        return authorize(socialType, socialProfile, tokenType, scopes, attributes)
    }

    private fun authorize(
        socialType: SocialType,
        socialProfile: SocialProfile,
        tokenType: TokenType,
        scopes: List<String>,
        attributes: Map<String, Any>
    ): RefreshableToken {

        val issuer = tokenIssuerProvider.get(tokenType).apply {
            setSubject(socialType.name)
            setIdentity(socialProfile.id)
            setScopes(scopes)
            setExtras(attributes)
        }
        return issuer.issue()
    }
}
