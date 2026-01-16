package com.upsaclay.authentication

import com.upsaclay.authentication.domain.entity.AuthenticationException
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.AUTH_USER_NOT_FOUND
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.EMAIL_ALREADY_IN_USE
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.INVALID_CREDENTIALS
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.REGISTRATION_FAILED
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.USER_DISABLED
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.USER_NOT_WHITE_LISTED
import com.upsaclay.common.utils.mapExceptionErrorMessage

fun mapAuthException(e: Exception): Int {
    return if (e is AuthenticationException) {
        when(e.error) {
            INVALID_CREDENTIALS -> R.string.invalid_credentials_error
            USER_DISABLED -> R.string.user_disabled_error
            EMAIL_ALREADY_IN_USE -> R.string.email_already_in_use_error
            USER_NOT_WHITE_LISTED -> R.string.user_not_white_listed_error
            AUTH_USER_NOT_FOUND -> R.string.auth_user_not_found_error
            REGISTRATION_FAILED -> R.string.auth_user_not_found_error
            else -> com.upsaclay.common.R.string.unknown_error
        }
    } else {
        mapExceptionErrorMessage(e)
    }
}