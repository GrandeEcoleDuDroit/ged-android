package com.upsaclay.authentication.domain.entity.exception

class InvalidCredentialsException(
    override val message: String? = null,
    override val cause: Throwable? = null,
) : Exception()

data class AuthenticationException(
    val type: AuthExceptionType,
    val exception: Exception
): Exception(exception.message, exception.cause) {
    enum class AuthExceptionType {
        INVALID_CREDENTIALS_EXCEPTION,
        USER_DISABLED_EXCEPTION,
        EMAIL_ALREADY_IN_USE_EXCEPTION,
        USER_NOT_WHITE_LISTED_EXCEPTION
    }
}