package com.upsaclay.common.domain.entity

data class CustomException(
    val type: ExceptionType,
    val exception: Exception
): Exception(exception.message, exception.cause) {
    enum class ExceptionType {
        INTERNAL_SERVER_EXCEPTION,
        TOO_MANY_REQUEST_EXCEPTION,
        FORBIDDEN_EXCEPTION,
        BAD_REQUEST_EXCEPTION,
        UNAUTHORIZED_EXCEPTION,
        RESOURCE_NOT_FOUND_EXCEPTION,
        TIMED_OUT_EXCEPTION,
        CANNOT_CONNECT_TO_HOST_EXCEPTION,
        ANY_NETWORK_EXCEPTION,
        CURRENT_USER_NOT_FOUND_EXCEPTION,
        UNKNOWN_EXCEPTION
    }
}