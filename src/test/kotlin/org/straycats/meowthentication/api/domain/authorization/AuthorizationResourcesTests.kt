package org.straycats.meowthentication.api.domain.authorization

import org.straycats.meowthentication.api.domain.authorization.api.AuthorizationResources
import org.straycats.meowthentication.api.domain.token.TokenType
import java.util.UUID

class AuthorizationResourcesTests

fun AuthorizationResources.Request.CodeAuthorization.Companion.aFixture() =
    AuthorizationResources.Request.CodeAuthorization(
        UUID.randomUUID().toString(),
        emptyList(),
        TokenType.JWT,
        emptyMap()
    )

fun AuthorizationResources.Request.TokenAuthorization.Companion.aFixture() =
    AuthorizationResources.Request.TokenAuthorization(
        UUID.randomUUID().toString(),
        emptyList(),
        TokenType.JWT,
        emptyMap()
    )
