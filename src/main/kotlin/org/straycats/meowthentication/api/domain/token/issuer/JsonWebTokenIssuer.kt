package org.straycats.meowthentication.api.domain.token.issuer

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.straycats.meowthentication.api.config.AppEnvironment
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.api.domain.token.TokenType
import java.time.Instant
import java.util.UUID

class JsonWebTokenIssuer(
    private val tokenEnv: AppEnvironment.Token.JWT
) : TokenIssuer {
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
}
