package com.upsaclay.authentication

import com.upsaclay.authentication.domain.entity.exception.AuthenticationException
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.EMAIL_ALREADY_IN_USE_EXCEPTION
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.INVALID_CREDENTIALS_EXCEPTION
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.USER_DISABLED_EXCEPTION
import com.upsaclay.authentication.domain.entity.exception.AuthenticationException.AuthExceptionType.USER_NOT_WHITE_LISTED_EXCEPTION
import com.upsaclay.common.utils.mapException

fun mapAuthException(e: Exception): Int {
    return if (e is AuthenticationException) {
        when(e.type) {
            INVALID_CREDENTIALS_EXCEPTION -> R.string.invalid_credentials_error
            USER_DISABLED_EXCEPTION -> R.string.user_disabled_error
            EMAIL_ALREADY_IN_USE_EXCEPTION -> R.string.email_already_in_use_error
            USER_NOT_WHITE_LISTED_EXCEPTION -> R.string.user_not_white_listed_error
            else -> com.upsaclay.common.R.string.unknown_error
        }
    } else {
        mapException(e)
    }
}