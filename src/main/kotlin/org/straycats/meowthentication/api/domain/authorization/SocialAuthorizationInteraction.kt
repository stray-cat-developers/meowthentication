package org.straycats.meowthentication.api.domain.authorization

import org.springframework.stereotype.Service
import org.straycats.meowthentication.api.domain.authentication.SocialAuthenticationInteraction
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.api.domain.token.TokenType
import org.straycats.meowthentication.api.domain.token.issuer.TokenIssuerProvider

@Service
class SocialAuthorizationInteraction(
    private val socialAuthenticationInteraction: SocialAuthenticationInteraction,
    private val tokenIssuerProvider: TokenIssuerProvider
) {

    fun authorizeWithCode(
        socialType: SocialType,
        code: String,
        tokenType: TokenType,
        scopes: List<String>,
        attributes: Map<String, Any>
    ): RefreshableToken {
        val authentication = socialAuthenticationInteraction.authentication(socialType, code)
        val socialProfile = socialAuthenticationInteraction.getProfile(socialType, authentication.access)

        return authorize(socialType, socialProfile, tokenType, scopes, attributes)
    }

    fun authorizeWithToken(
        socialType: SocialType,
        accessToken: String,
        tokenType: TokenType,
        scopes: List<String>,
        attributes: Map<String, Any>
    ): RefreshableToken {
        val socialProfile = socialAuthenticationInteraction.getProfile(socialType, accessToken)
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
            setIdentity(socialProfile.providerUserId)
            setScopes(scopes)
            setExtras(attributes)
        }
        return issuer.issue()
    }
}
