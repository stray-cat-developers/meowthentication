package org.straycats.meowthentication.api.common

class NormalError(
    errorCode: ErrorCode,
    message: String? = null,
    override var causeBy: Map<String, Any?>? = null,
) : ErrorSource {
    override val message = message ?: errorCode.summary
    override val code = errorCode.name
    override var refCode: String? = null
}
