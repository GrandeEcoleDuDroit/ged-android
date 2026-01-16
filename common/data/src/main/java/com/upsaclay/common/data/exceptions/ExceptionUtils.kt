package com.upsaclay.common.data.exceptions

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.ANY_NETWORK_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.BAD_REQUEST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.CANNOT_CONNECT_TO_HOST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.FORBIDDEN_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.INTERNAL_SERVER_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.RESOURCE_NOT_FOUND_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.TIMED_OUT_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.TOO_MANY_REQUEST_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.UNAUTHORIZED_EXCEPTION
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.UNKNOWN_EXCEPTION
import java.net.ConnectException
import java.net.SocketTimeoutException

fun mapFirebaseException(exception: Exception): CustomException {
    return when (exception) {
        is FirebaseNetworkException -> CustomException(ANY_NETWORK_EXCEPTION, exception)
        is FirebaseTooManyRequestsException -> CustomException(TOO_MANY_REQUEST_EXCEPTION, exception)
        is FirebaseFirestoreException -> {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNKNOWN -> CustomException(UNKNOWN_EXCEPTION, exception)
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> CustomException(FORBIDDEN_EXCEPTION, exception)
                FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> CustomException(TOO_MANY_REQUEST_EXCEPTION, exception)
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> CustomException(UNAUTHORIZED_EXCEPTION, exception)
                else -> CustomException(UNKNOWN_EXCEPTION, exception)
            }
        }
        else -> CustomException(UNKNOWN_EXCEPTION, exception)
    }
}

fun mapServerException(exception: Exception): CustomException {
    return when (exception) {
        is ConnectException -> CustomException(CANNOT_CONNECT_TO_HOST_EXCEPTION, exception)
        is SocketTimeoutException -> CustomException(TIMED_OUT_EXCEPTION, exception)
        is ServerException -> {
            when (exception.httpCode) {
                400 -> CustomException(BAD_REQUEST_EXCEPTION, exception)
                401 -> CustomException(UNAUTHORIZED_EXCEPTION, exception)
                403 -> CustomException(FORBIDDEN_EXCEPTION, exception)
                404 -> CustomException(RESOURCE_NOT_FOUND_EXCEPTION, exception)
                else -> CustomException(INTERNAL_SERVER_EXCEPTION, exception)
            }
        }
        else -> CustomException(UNKNOWN_EXCEPTION, exception)
    }
}
