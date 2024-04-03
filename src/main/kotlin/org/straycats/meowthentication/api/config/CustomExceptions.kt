package org.straycats.meowthentication.api.config

import org.straycats.meowthentication.api.common.ErrorCode
import org.straycats.meowthentication.api.common.ErrorSource
import org.straycats.meowthentication.api.common.NormalError

open class CustomException(val error: ErrorSource) : RuntimeException(error.message)

/**
 * Human Exception
 */
open class HumanException(error: ErrorSource) : CustomException(error)
class DataNotFindException : HumanException {
    constructor(message: String) : super(NormalError(ErrorCode.HD00, message))
    constructor(id: String, message: String) : super(NormalError(ErrorCode.HD00, message, mapOf("id" to id)))
    constructor(id: Long, message: String) : this(id.toString(), message)
}

class PreconditionFailException(message: String) : HumanException(NormalError(ErrorCode.HD02, message))

// ignore in sentry
class DataNotSearchedException : HumanException {
    constructor(message: String) : super(NormalError(ErrorCode.HD01, message))
    constructor(source: String, message: String) : super(NormalError(ErrorCode.HD01, message, mapOf("search source" to source)))
    constructor(id: Long, message: String) : this(id.toString(), message)
}

class MissingRequestXHeaderException(headerName: String) : HumanException(NormalError(ErrorCode.HI02, "Missing request header $headerName"))

class InvalidArgumentException(message: String) : HumanException(NormalError(ErrorCode.HI01, message))

open class SystemException(error: ErrorSource) : CustomException(error)
class DevelopMistakeException : SystemException {
    constructor(errorCode: ErrorCode) : super(NormalError(errorCode, errorCode.summary))
    constructor(message: String, causeBy: Map<String, Any?>? = null) : super(NormalError(ErrorCode.PD01, message, causeBy))
}

open class CommunicationException(error: ErrorSource) : CustomException(error)
class ClientException(target: String, message: String, code: String? = null) : CommunicationException(
    NormalError(
        ErrorCode.C000,
        message,
        causeBy = mapOf(
            "target" to target,
            "clientErrorCode" to code,
        ),
    ).apply {
        refCode = code
    },
)
class ConnectionTimeoutException(target: String, timeoutConfig: Int, url: String) : CommunicationException(
    NormalError(
        ErrorCode.CT01,
        "$target Connection fail",
        causeBy = mapOf(
            "target" to target,
            "url" to url,
            "timeout" to timeoutConfig,
        ),
    ),
)

class ReadTimeoutException(target: String, timeoutConfig: Int, url: String) : CommunicationException(
    NormalError(
        ErrorCode.CT02,
        "$target Data not Received",
        causeBy = mapOf(
            "target" to target,
            "url" to url,
            "timeout" to timeoutConfig,
        ),
    ),
)

open class AsyncException(message: String, causeBy: Map<String, Any?>? = null) : CustomException(
    NormalError(
        ErrorCode.SA00,
        message,
        causeBy,
    ),
)

/**
 * Policy Exception
 */
open class PolicyException(error: ErrorSource) : CustomException(error)

/**
 * UnAuthorized Exception
 */
open class UnAuthorizedException(error: ErrorSource) : CustomException(error)

class PermissionException : UnAuthorizedException {
    constructor() : super(NormalError(ErrorCode.HA00, "Access denied"))
    constructor(message: String) : super(NormalError(ErrorCode.HA01, message))
}

class AccessDeniedException : UnAuthorizedException(NormalError(ErrorCode.HA00, "Unauthorized"))
