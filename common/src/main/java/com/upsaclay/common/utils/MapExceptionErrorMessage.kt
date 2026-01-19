package com.upsaclay.common.utils

import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.BAD_REQUEST
import com.upsaclay.common.domain.entity.CustomException.CustomError.CANNOT_CONNECT_TO_HOST
import com.upsaclay.common.domain.entity.CustomException.CustomError.CURRENT_USER_NOT_FOUND
import com.upsaclay.common.domain.entity.CustomException.CustomError.FORBIDDEN
import com.upsaclay.common.domain.entity.CustomException.CustomError.INTERNAL_SERVER
import com.upsaclay.common.domain.entity.CustomException.CustomError.NETWORK
import com.upsaclay.common.domain.entity.CustomException.CustomError.RESOURCE_NOT_FOUND
import com.upsaclay.common.domain.entity.CustomException.CustomError.TIMED_OUT
import com.upsaclay.common.domain.entity.CustomException.CustomError.TOO_MANY_REQUEST
import com.upsaclay.common.domain.entity.CustomException.CustomError.UNAUTHORIZED
import com.upsaclay.common.domain.entity.CustomException.CustomError.UNKNOWN
import kotlinx.coroutines.TimeoutCancellationException

fun mapExceptionErrorMessage(e: Exception): Int {
    return when(e) {
        is CustomException -> mapCustomExceptionErrorMessage(e)
        is TimeoutCancellationException -> R.string.timed_out_error
        else -> R.string.unknown_error
    }
}

private fun mapCustomExceptionErrorMessage(e: CustomException): Int {
    return when(e.error) {
        INTERNAL_SERVER -> R.string.internal_server_error
        TOO_MANY_REQUEST -> R.string.too_many_request_error
        FORBIDDEN -> R.string.forbidden_error
        BAD_REQUEST -> R.string.bad_request_error
        UNAUTHORIZED -> R.string.unauthorized_error
        RESOURCE_NOT_FOUND -> R.string.user_not_found_title_dialog
        TIMED_OUT -> R.string.timed_out_error
        CANNOT_CONNECT_TO_HOST -> R.string.cannot_connect_to_host_error
        NETWORK -> R.string.any_network_error
        CURRENT_USER_NOT_FOUND -> R.string.current_user_not_found_error
        UNKNOWN -> R.string.unknown_error
        else -> R.string.unknown_error
    }
}