package org.straycats.meowthentication.api.domain.authentication.provider.kakao

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.core5.http.HttpHeaders
import org.slf4j.LoggerFactory
import org.springframework.web.util.UriComponentsBuilder
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialClient
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.utils.Jackson
import org.straycats.meowthentication.utils.RestClientSupport
import org.straycats.meowthentication.utils.fromJson

class StableKakaoClient(
    private val env: AppEnvironment.Client.Kakao,
    private val httpClient: CloseableHttpClient
) : RestClientSupport(
    Jackson.getMapper(),
    env.logging,
    LoggerFactory.getLogger(StableKakaoClient::class.java)
),
    SocialClient {
    override fun authorize(code: String, redirectedUrl: String?): RefreshableToken {
        val url = "${env.authHost}/oauth/token"
        val headers = listOf(
            "Content-Type" to "application/x-www-form-urlencoded"
        )
        val params = listOf(
            "grant_type" to "authorization_code",
            "client_id" to env.clientId,
            "redirect_uri" to UriComponentsBuilder.fromHttpUrl(redirectedUrl!!).replaceQuery(null).build().toString(),
            "code" to code,
            "client_secret" to env.clientSecret
        )
        val result = httpClient.post(url, headers, params)
            .orElseThrow(KakaoClientResources.Reply.AuthenticationError::class.java)
            .fromJson<KakaoClientResources.Reply.Token>()

        return RefreshableToken(
            result.tokenType,
            result.accessToken,
            result.expiresIn,
            result.refreshToken,
            result.refreshTokenExpiresIn
        )
    }

    override fun getProfile(accessToken: String): SocialProfile {
        val url = "${env.host}/v2/user/me"
        val headers = listOf(
            HttpHeaders.AUTHORIZATION to "Bearer $accessToken"
        )
        val result = httpClient.get(url, headers)
            .orElseThrow()
            .fromJson<KakaoClientResources.Reply.Profile>()

        return SocialProfile(
            result.id,
            result.kakaoAccount?.email
        )
    }
}
