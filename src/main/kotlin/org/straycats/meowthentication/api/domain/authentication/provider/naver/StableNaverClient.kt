package org.straycats.meowthentication.api.domain.authentication.provider.naver

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.core5.http.HttpHeaders
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialClient
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.utils.Jackson
import org.straycats.meowthentication.utils.RestClientSupport
import org.straycats.meowthentication.utils.fromJson

class StableNaverClient(
    private val env: AppEnvironment.Client.Naver,
    private val httpClient: CloseableHttpClient
) : RestClientSupport(
    Jackson.getMapper(),
    env.logging,
    LoggerFactory.getLogger(StableNaverClient::class.java)
),
    SocialClient {

    override fun authorize(code: String, redirectedUrl: String?): RefreshableToken {
        val uri = "${env.authHost}/oauth2.0/token"
        val headers = listOf(
            HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED_VALUE
        )
        val params = listOf(
            "grant_type" to "authorization_code",
            "client_id" to env.clientId,
            "code" to code,
            "client_secret" to env.clientSecret,
            "state" to redirectedUrl!!
        )
        val result = httpClient.post(uri, headers, params)
            .orElseThrow(NaverClientResources.Reply.Error::class.java)
            .fromJson<NaverClientResources.Reply.Token>()

        return RefreshableToken(
            result.tokenType,
            result.accessToken,
            result.expiresInSeconds,
            result.refreshToken,
            result.expiresInSeconds,
        )
    }

    override fun getProfile(accessToken: String): SocialProfile {
        val uri = "${env.host}/v1/nid/me"
        val headers = listOf(
            HttpHeaders.AUTHORIZATION to "Bearer $accessToken"
        )
        val result = httpClient.get(uri, headers)
            .orElseThrow(NaverClientResources.Reply.Error::class.java)
            .fromJson<NaverClientResources.Reply.Profile>()

        return SocialProfile(
            result.naver!!.id,
            result.naver.email
        )
    }
}
