package org.straycats.meowthentication.api.domain.authentication.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.straycats.meowthentication.api.common.Reply
import org.straycats.meowthentication.api.domain.authentication.SocialAuthenticationInteraction
import org.straycats.meowthentication.api.domain.authentication.provider.SocialType
import org.straycats.meowthentication.utils.toReply

@Tag(name = "Social Authentication")
@RestController
@RequestMapping("/v1/authentication")
class SocialAuthenticationController(
    private val socialAuthenticationInteraction: SocialAuthenticationInteraction
) {
    @Operation(summary = "Authenticating by OAuth authorize code")
    @PostMapping("/social/{socialType}/code")
    fun authenticateWithCode(
        @PathVariable socialType: SocialType,
        @RequestBody request: AuthenticationResources.Request.CodeAuthentication,
    ): Reply<AuthenticationResources.Reply.SocialAuthentication> {
        val token = socialAuthenticationInteraction.authentication(socialType, request.code, request.redirectedUrl)
        val profile = socialAuthenticationInteraction.getProfile(socialType, token.access)
        return AuthenticationResources.Reply.SocialAuthentication.from(socialType, token, profile).toReply()
    }

    @Operation(description = "Authenticating by OAuth access token")
    @PostMapping("/social/{socialType}/access-token")
    fun authenticateWithToken(
        @PathVariable socialType: SocialType,
        @RequestBody request: AuthenticationResources.Request.TokenAuthentication,
    ): Reply<AuthenticationResources.Reply.SocialAuthentication> {
        val profile = socialAuthenticationInteraction.getProfile(socialType, request.accessToken)
        return AuthenticationResources.Reply.SocialAuthentication.from(socialType, profile).toReply()
    }
}
