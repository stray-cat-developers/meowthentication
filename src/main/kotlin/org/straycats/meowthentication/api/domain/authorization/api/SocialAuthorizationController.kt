package org.straycats.meowthentication.api.domain.authorization.api

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.domain.authorization.SocialAuthorizationInteraction
import org.straycats.meowthentication.api.domain.social.provider.SocialType
import org.straycats.meowthentication.api.domain.token.RefreshableToken
import org.straycats.meowthentication.utils.toReply

@RestController
@RequestMapping("v1/authorization/social/{socialType}")
class SocialAuthorizationController(
    private val socialAuthorizationInteraction: SocialAuthorizationInteraction
) {

    @Operation(summary = "Authorizing by OAuth authorize code")
    @PostMapping("code")
    fun authorizeWithCode(
        @PathVariable socialType: SocialType,
        @RequestBody request: AuthorizationResources.Request.CodeAuthorization
    ): Reply<RefreshableToken> {
        val authorization = socialAuthorizationInteraction.authorizeWithCode(
            socialType,
            request.code,
            request.issueType,
            request.scopes,
            request.attributes,
            request.redirectUrl
        )

        return authorization.toReply()
    }

    @Operation(summary = "Authorizing by OAuth access token")
    @PostMapping("access-token")
    fun authorizeWithToken(
        @PathVariable socialType: SocialType,
        @RequestBody request: AuthorizationResources.Request.TokenAuthorization
    ): Reply<RefreshableToken> {
        val authorization = socialAuthorizationInteraction.authorizeWithToken(
            socialType,
            request.accessToken,
            request.issueType,
            request.scopes,
            request.attributes
        )

        return authorization.toReply()
    }
}
