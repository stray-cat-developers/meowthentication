package org.straycats.meowthentication.api.domain.authentication

import org.straycats.meowthentication.api.domain.authentication.api.AuthenticationResources
import java.util.UUID

class AuthenticationResourcesTests

internal fun AuthenticationResources.Request.TokenAuthentication.Companion.aFixture() =
    AuthenticationResources.Request.TokenAuthentication(
        UUID.randomUUID().toString()
    )

internal fun AuthenticationResources.Request.CodeAuthentication.Companion.aFixture() =
    AuthenticationResources.Request.CodeAuthentication(
        code = UUID.randomUUID().toString(),
        redirectedUrl = "http://localdummy.test"
    )
