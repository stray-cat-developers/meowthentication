package org.straycats.meowthentication.api.domain.token.issuer

import org.straycats.meowthentication.api.domain.token.RefreshableToken

interface TokenIssuer {
    fun setSubject(subject: String)
    fun setExtras(attributes: Map<String, Any>)
    fun setIdentity(identity: String)
    fun setScopes(scopes: List<String>)
    fun issue(): RefreshableToken
}
