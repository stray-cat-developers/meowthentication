package org.straycats.meowthentication.api.domain.authentication.provider.apple

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.authentication.AuthenticationToken
import org.straycats.meowthentication.api.domain.authentication.SocialProfile
import org.straycats.meowthentication.api.domain.authentication.provider.SocialClient
import org.straycats.meowthentication.utils.Jackson
import org.straycats.meowthentication.utils.RestClientSupport
import org.straycats.meowthentication.utils.fromJson
import org.straycats.meowthentication.utils.plusHour
import java.security.KeyFactory
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.Base64
import java.util.Date

class StableAppleClient(
    private val env: AppEnvironment.Client.Apple,
    private val httpClient: CloseableHttpClient
) : SocialClient,
    RestClientSupport(
        Jackson.getMapper(),
        env.logging,
        LoggerFactory.getLogger(StableAppleClient::class.java)
    ) {

    override fun authorize(code: String, redirectedUrl: String?): AuthenticationToken {
        val url = "${env.host}/auth/token"
        val headers = listOf(
            HttpHeaders.CONTENT_TYPE to MediaType.APPLICATION_FORM_URLENCODED_VALUE
        )
        val params = listOf(
            "grant_type" to "authorization_code",
            "client_id" to env.clientId,
            "code" to code,
            "client_secret" to generateClientSecret()
        )

        val result = httpClient.post(url, headers, params)
            .orElseThrow()
            .fromJson<AppleClientResources.Reply.Token>()

        return AuthenticationToken(
            result.tokenType,
            result.idToken,
            result.expiresIn,
            result.refreshToken,
            result.expiresIn
        )
    }

    override fun getProfile(accessToken: String): SocialProfile {
        val decoded = JWT.decode(accessToken)
        verify(decoded)
        return SocialProfile(
            decoded.subject,
            decoded.getClaim("email").asString()
        )
    }

    private fun verify(decoded: DecodedJWT) {
        try {
            val jwk = JwkProviderBuilder(APPLE_PROVIDER).build().get(decoded.keyId)
            val jwtVerifier = JWT.require(Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null))
                .acceptLeeway(1000)
                .build()
            jwtVerifier.verify(decoded.token)
        } catch (e: Exception) {
            throw IllegalStateException("failed to verify token")
        }
    }

    private fun generateClientSecret(): String {
        // pubkey
        var encoded =
            Base64.getDecoder().decode(
                String(Base64.getDecoder().decode(env.clientSecretPublicKey))
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace(System.lineSeparator(), "")
                    .replace("-----END PUBLIC KEY-----", "")
            )

        var kf = KeyFactory.getInstance("EC")
        val pubKeySpec = X509EncodedKeySpec(encoded)
        val publicKey = kf.generatePublic(pubKeySpec) as ECPublicKey
        // privkey
        encoded =
            Base64.getDecoder().decode(
                String(Base64.getDecoder().decode(env.clientSecretPrivateKey))
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "")
            )
        kf = KeyFactory.getInstance("EC")
        val privateKeySpec = PKCS8EncodedKeySpec(encoded)
        val privateKey = kf.generatePrivate(privateKeySpec) as ECPrivateKey
        val algorithm = Algorithm.ECDSA256(publicKey, privateKey)
        val now = Date()
        return JWT.create()
            .withIssuedAt(now)
            .withExpiresAt(now.plusHour(12))
            .withIssuer(env.developerTeamId)
            .withAudience("https://appleid.apple.com")
            .withSubject(env.clientId)
            .withKeyId(env.developerKeyId)
            .sign(algorithm)
    }

    companion object {
        const val APPLE_PROVIDER = "https://appleid.apple.com/auth/keys"
    }
}
