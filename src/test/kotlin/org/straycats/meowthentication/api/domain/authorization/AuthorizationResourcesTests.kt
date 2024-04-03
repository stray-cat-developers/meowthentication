package org.straycats.meowthentication.api.domain.authorization

import org.straycats.meowthentication.api.domain.authorization.api.AuthorizationResources
import org.straycats.meowthentication.api.domain.token.TokenType
import java.util.UUID

class AuthorizationResourcesTests

fun AuthorizationResources.Request.CodeAuthentication.Companion.aFixture() =
    AuthorizationResources.Request.CodeAuthentication(
        UUID.randomUUID().toString(),
        emptyList(),
        TokenType.JWT,
        emptyMap()
    )

fun AuthorizationResources.Request.TokenAuthentication.Companion.aFixture() =
    AuthorizationResources.Request.TokenAuthentication(
        UUID.randomUUID().toString(),
        emptyList(),
        TokenType.JWT,
        emptyMap()
    )
