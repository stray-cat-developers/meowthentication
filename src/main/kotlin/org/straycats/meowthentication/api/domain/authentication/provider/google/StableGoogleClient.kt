package org.straycats.meowthentication.api.domain.authentication.provider.google

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import org.slf4j.LoggerFactory
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialClient
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.utils.Jackson
import org.straycats.meowthentication.utils.RestClientSupport

class StableGoogleClient(
    private val env: AppEnvironment.Client.Google,
) : SocialClient,
    RestClientSupport(
        Jackson.getMapper(),
        env.logging,
        LoggerFactory.getLogger(StableGoogleClient::class.java)
    ) {
    override fun authorize(code: String, redirectedUrl: String?): RefreshableToken {
        val flow = GoogleAuthorizationCodeFlow.Builder(
            NetHttpTransport(),
            GsonFactory(),
            env.clientId,
            env.clientSecret,
            emptyList()
        )
            .build()
        return try {
            val response = flow.newTokenRequest(code)
                .apply {
                    redirectedUrl?.let { this.setRedirectUri(it) }
                }.execute()
            RefreshableToken(
                response.tokenType,
                response.accessToken,
                response.expiresInSeconds,
                response.refreshToken,
                response.expiresInSeconds
            )
        } catch (e: Exception) {
            throw IllegalStateException("failed to authorize code")
        }
    }

    override fun getProfile(accessToken: String): SocialProfile {
        val id = try {
            val verifier = GoogleIdTokenVerifier.Builder(NetHttpTransport(), GsonFactory())
                .setAudience(env.clientId.split(",").toList())
                .build()
            verifier.verify(accessToken)
        } catch (e: Exception) {
            throw IllegalStateException("failed to authorize code")
        }

        return SocialProfile(
            id.payload.subject,
            id.payload.email
        )
    }
}
