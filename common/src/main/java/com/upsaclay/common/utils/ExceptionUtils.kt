package com.upsaclay.common.utils

import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.ANY_NETWORK_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.BAD_REQUEST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.CANNOT_CONNECT_TO_HOST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.CURRENT_USER_NOT_FOUND_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.FORBIDDEN_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.INTERNAL_SERVER_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.RESOURCE_NOT_FOUND_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.TIMED_OUT_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.TOO_MANY_REQUEST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.UNAUTHORIZED_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.UNKNOWN_EXCEPTION

fun mapException(e: Exception): Int {
    return if (e is CustomException) {
        when(e.type) {
            INTERNAL_SERVER_EXCEPTION -> R.string.internal_server_error
            TOO_MANY_REQUEST_EXCEPTION -> R.string.too_many_request_error
            FORBIDDEN_EXCEPTION -> R.string.forbidden_error
            BAD_REQUEST_EXCEPTION -> R.string.bad_request_error
            UNAUTHORIZED_EXCEPTION -> R.string.unauthorized_error
            RESOURCE_NOT_FOUND_EXCEPTION -> R.string.user_not_found_title_dialog
            TIMED_OUT_EXCEPTION -> R.string.timed_out_error
            CANNOT_CONNECT_TO_HOST_EXCEPTION -> R.string.cannot_connect_to_host_error
            ANY_NETWORK_EXCEPTION -> R.string.any_network_error
            CURRENT_USER_NOT_FOUND_EXCEPTION -> R.string.current_user_not_found_error
            UNKNOWN_EXCEPTION -> R.string.unknown_error
            else -> R.string.unknown_error
        }
    } else {
        R.string.unknown_error
    }
}