package com.upsaclay.common.domain.entity

data class CustomException(
    val error: CustomError,
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception(message, cause) {
    constructor(error: CustomError, exception: Exception): this(error, exception.message, exception.cause)

    enum class CustomError {
        INTERNAL_SERVER,
        TOO_MANY_REQUEST,
        FORBIDDEN,
        BAD_REQUEST,
        UNAUTHORIZED,
        RESOURCE_NOT_FOUND,
        TIMED_OUT,
        CANNOT_CONNECT_TO_HOST,
        NETWORK,
        CURRENT_USER_NOT_FOUND,
        UNKNOWN
    }
}