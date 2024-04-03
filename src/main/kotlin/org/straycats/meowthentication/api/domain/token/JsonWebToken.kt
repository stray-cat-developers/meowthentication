package org.straycats.meowthentication.api.domain.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.token.issuer.TokenIssuer
import org.straycats.meowthentication.api.domain.token.verifier.TokenVerifier
import org.straycats.meowthentication.utils.fromJson
import java.time.Instant
import java.util.Base64
import java.util.UUID

class JsonWebToken(
    private val tokenEnv: AppEnvironment.Token.JWT
) : TokenIssuer, TokenVerifier {
    private val type = TokenType.JWT
    private val algorithm = Algorithm.HMAC256(tokenEnv.secret)

    private var attributes: Map<String, Any> = emptyMap()
    private var identity: String? = null
    private var subject: String? = null
    private var scopes: List<String> = emptyList()

    private val accessTtl = tokenEnv.ttlInSeconds.toLong()
    private val refreshTtl = tokenEnv.refreshableInSeconds.toLong()
    override fun setSubject(subject: String) {
        this.subject = subject
    }

    override fun setExtras(attributes: Map<String, Any>) {
        this.attributes = attributes
    }

    override fun setIdentity(identity: String) {
        this.identity = identity
    }

    override fun setScopes(scopes: List<String>) {
        this.scopes = scopes
    }

    override fun issue(): RefreshableToken {
        requireNotNull(identity) { "identity is required" }
        val issuedAt = Instant.now()
        val tokenId = UUID.randomUUID().toString()
        val payload = attributes.toMutableMap().apply {
            identity?.let { put("identity", it) }
            put("scopes", scopes)
        }
        val accessToken = JWT.create()
            .withIssuer(tokenEnv.issuer)
            .withIssuedAt(issuedAt)
            .withExpiresAt(issuedAt.plusSeconds(accessTtl))
            .withAudience(identity)
            .withJWTId(tokenId)
            .withPayload(payload)
            .apply {
                subject?.let { withSubject(it) }
            }
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withIssuer(tokenEnv.issuer)
            .withIssuedAt(issuedAt)
            .withExpiresAt(issuedAt.plusSeconds(refreshTtl))
            .withJWTId(tokenId)
            .sign(algorithm)

        return RefreshableToken(
            type.name,
            accessToken,
            accessTtl,
            refreshToken,
            refreshTtl
        )
    }

    override fun verify(token: String): Boolean {
        val verifier = JWT.require(algorithm)
            .withIssuer(tokenEnv.issuer)
            .acceptLeeway(tokenEnv.leewayInMils.toLong())
            .build()

        return try {
            verifier.verify(token)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun verifyOrThrow(token: String) {
        if (verify(token).not()) {
            throw IllegalArgumentException("token is not valid")
        }
    }

    override fun getClaims(token: String): Map<String, Any?> {
        verifyOrThrow(token)
        return try {
            val payload = JWT.decode(token).payload
            String(Base64.getUrlDecoder().decode(payload)).fromJson()
        } catch (e: Exception) {
            throw IllegalArgumentException("token is not valid")
        }
    }
}
