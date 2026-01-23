package com.upsaclay.authentication.domain.entity

data class AuthenticationException(
    val error: AuthenticationError,
    override val message: String? = null,
    override val cause: Throwable? = null
): Exception(message, cause) {
    constructor(error: AuthenticationError, exception: Exception): this(error, exception.message, exception.cause)

    enum class AuthenticationError {
        INVALID_CREDENTIALS,
        USER_DISABLED,
        EMAIL_ALREADY_IN_USE,
        USER_NOT_WHITE_LISTED,
        AUTH_USER_NOT_FOUND,
        REGISTRATION_FAILED
    }
}