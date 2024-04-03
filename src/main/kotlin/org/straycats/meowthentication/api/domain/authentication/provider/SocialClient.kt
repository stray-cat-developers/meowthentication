package org.straycats.meowthentication.api.domain.authentication.provider

import org.straycats.meowthentication.api.domain.authentication.AuthenticationToken
import org.straycats.meowthentication.api.domain.authentication.SocialProfile

interface SocialClient {
    fun authorize(code: String, redirectedUrl: String?): AuthenticationToken
    fun getProfile(accessToken: String): SocialProfile
}
