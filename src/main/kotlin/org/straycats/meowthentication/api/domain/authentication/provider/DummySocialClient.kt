package org.straycats.meowthentication.api.domain.authentication.provider

import org.straycats.meowthentication.api.domain.authentication.AuthenticationToken
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import java.util.UUID

class DummySocialClient : SocialClient {
    override fun authorize(code: String, redirectedUrl: String?): AuthenticationToken {
        return dummyToken()
    }

    override fun getProfile(accessToken: String): SocialProfile {
        return SocialProfile(
            UUID.randomUUID().toString(),
            "dummy@dummy.com"
        )
    }

    private fun dummyToken(): AuthenticationToken {
        return AuthenticationToken(
            tokenType = "bearer",
            access = UUID.randomUUID().toString(),
            accessExpiresInSeconds = 3600L,
            refresh = UUID.randomUUID().toString(),
            refreshExpiresInSeconds = 3600L,
        )
    }
}
