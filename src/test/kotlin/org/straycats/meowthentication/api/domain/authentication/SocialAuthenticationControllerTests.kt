package org.straycats.meowthentication.api.domain.authentication

import io.kotest.assertions.asClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.straycats.meowthentication.api.config.FlowTestSupport
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType

class SocialAuthenticationControllerTests : FlowTestSupport() {

    @Test
    fun appleAuthentication() {
        // Given
        val flow = AuthenticationFlow(mockMvc)

        // When
        val provider = SocialType.APPLE
        val actual = flow.authenticateWithCode(provider)

        // Then
        actual.asClue {
            it.type shouldBe provider
            it.identifyKey.isBlank() shouldBe false
            it.email.isNullOrBlank() shouldBe false
        }
    }

    @Test
    fun googleAuthentication() {
        // Given
        val flow = AuthenticationFlow(mockMvc)

        // When
        val provider = SocialType.GOOGLE
        val actual = flow.authenticateWithToken(provider)

        // Then
        actual.asClue {
            it.type shouldBe provider
            it.identifyKey.isBlank() shouldBe false
            it.email.isNullOrBlank() shouldBe false
        }
    }

    @Test
    fun kakaoAuthentication() {

        // Given
        val flow = AuthenticationFlow(mockMvc)

        // When
        val provider = SocialType.KAKAO
        val actual = flow.authenticateWithCode(provider)

        // Then
        actual.asClue {
            it.type shouldBe provider
            it.identifyKey.isBlank() shouldBe false
            it.email.isNullOrBlank() shouldBe false
        }
    }

    @Test
    fun naverAuthentication() {
        // Given
        val flow = AuthenticationFlow(mockMvc)
        val provider = SocialType.NAVER

        // When
        val actual = flow.authenticateWithCode(provider)

        // Then
        actual.asClue {
            it.type shouldBe provider
            it.identifyKey.isBlank() shouldBe false
            it.email.isNullOrBlank() shouldBe false
        }
    }
}
