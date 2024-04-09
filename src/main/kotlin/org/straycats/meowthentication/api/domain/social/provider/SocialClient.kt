package org.straycats.meowthentication.api.domain.social.provider

import org.straycats.meowthentication.api.domain.social.SocialProfile
import org.straycats.meowthentication.api.domain.token.RefreshableToken

interface SocialClient {
    fun authorize(code: String, redirectedUrl: String?): RefreshableToken
    fun getProfile(accessToken: String): SocialProfile
}
