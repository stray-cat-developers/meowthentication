package org.straycats.meowthentication.api.domain.token

import io.kotest.matchers.ints.shouldBeGreaterThan
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.straycats.meowthentication.api.config.FlowTestSupport
import org.straycats.meowthentication.api.domain.authorization.AuthorizationFlow
import org.straycats.meowthentication.api.domain.social.provider.SocialType

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class JsonWebTokenControllerTests : FlowTestSupport() {

    private lateinit var token: String

    @BeforeEach
    fun beforeEach() {
        token = AuthorizationFlow(mockMvc)
            .authorizeWithToken(SocialType.entries.random())
            .access
    }

    @Test
    fun getClaims() {
        // Given
        val flow = TokenFlow(mockMvc)

        // When
        val actual = flow.getClaims(token)

        // Then
        actual.entries.size shouldBeGreaterThan 0
    }
}
