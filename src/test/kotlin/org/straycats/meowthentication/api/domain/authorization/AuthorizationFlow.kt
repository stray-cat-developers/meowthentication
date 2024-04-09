package org.straycats.meowthentication.api.domain.authorization

import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.domain.authorization.api.AuthorizationResources
import org.straycats.meowthentication.api.domain.authorization.api.SocialAuthorizationController
import org.straycats.meowthentication.api.domain.social.provider.SocialType
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.utils.fromJson
import org.straycats.meowthentication.utils.toJson

class AuthorizationFlow(
    private val mockMvc: MockMvc
) {

    fun authorizeWithCode(
        socialType: SocialType,
        request: AuthorizationResources.Request.CodeAuthorization = AuthorizationResources.Request.CodeAuthorization.aFixture()
    ): RefreshableToken {
        val uri = linkTo<SocialAuthorizationController> { authorizeWithCode(socialType, request) }
            .toUri()

        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<RefreshableToken>>()
            .content!!
    }

    fun authorizeWithToken(
        socialType: SocialType,
        request: AuthorizationResources.Request.TokenAuthorization = AuthorizationResources.Request.TokenAuthorization.aFixture()
    ): RefreshableToken {
        val uri = linkTo<SocialAuthorizationController> { authorizeWithToken(socialType, request) }
            .toUri()

        return mockMvc.post(uri) {
            contentType = MediaType.APPLICATION_JSON
            content = request.toJson()
        }.andExpect {
            status { is2xxSuccessful() }
        }.andReturn()
            .response
            .contentAsString
            .fromJson<Reply<RefreshableToken>>()
            .content!!
    }
}
