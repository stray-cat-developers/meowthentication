package org.straycats.meowthentication.api.domain.authentication.provider

import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.token.RefreshableToken

interface SocialClient {
    fun authorize(code: String, redirectedUrl: String?): RefreshableToken
    fun getProfile(accessToken: String): SocialProfile
}
