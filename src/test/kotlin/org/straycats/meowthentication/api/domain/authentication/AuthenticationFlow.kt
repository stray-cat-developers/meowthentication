package org.straycats.meowthentication.api.domain.authentication

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.domain.authentication.api.AuthenticationResources
import org.straycats.meowthentication.api.domain.authentication.api.SocialAuthenticationController
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType
import org.straycats.meowthentication.utils.fromJson
import org.straycats.meowthentication.utils.toJson

class AuthenticationFlow(
    private val mockMvc: MockMvc
) {
    fun authenticateWithToken(
        provider: SocialType,
        request: AuthenticationResources.Request.TokenAuthentication =
            AuthenticationResources.Request.TokenAuthentication.aFixture()
    ): AuthenticationResources.Reply.SocialAuthentication {
        val uri = linkTo<SocialAuthenticationController> {
            authenticateWithToken(
                provider,
                request
            )
        }.toUri()

        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andDo {
            println()
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<AuthenticationResources.Reply.SocialAuthentication>>()
            .content!!
    }

    fun authenticateWithCode(
        provider: SocialType,
        request: AuthenticationResources.Request.CodeAuthentication =
            AuthenticationResources.Request.CodeAuthentication.aFixture()
    ): AuthenticationResources.Reply.SocialAuthentication {
        val uri = linkTo<SocialAuthenticationController> {
            authenticateWithCode(
                provider,
                request
            )
        }
            .toUri()
        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<AuthenticationResources.Reply.SocialAuthentication>>()
            .content!!
    }
}
