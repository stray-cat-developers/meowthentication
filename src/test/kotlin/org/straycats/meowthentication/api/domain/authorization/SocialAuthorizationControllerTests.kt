package org.straycats.meowthentication.api.domain.authorization

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.straycats.meowthentication.api.config.FlowTestSupport
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType
import org.straycats.meowthentication.api.domain.token.TokenType

class SocialAuthorizationControllerTests : FlowTestSupport() {

    @Test
    fun authorizeWithCode() {
        // Given
        val socialType = SocialType.APPLE
        val flow = AuthorizationFlow(mockMvc)

        // When
        val actual = flow.authorizeWithCode(socialType)

        // Then
        actual.tokenType shouldBe TokenType.JWT.name
    }

    @Test
    fun authorizeWithToken() {
        // Given
        val socialType = SocialType.APPLE
        val flow = AuthorizationFlow(mockMvc)

        // When
        val actual = flow.authorizeWithToken(socialType)

        // Then
        actual.tokenType shouldBe TokenType.JWT.name
    }
}
