package com.upsaclay.common.data.exceptions

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.BAD_REQUEST
import com.upsaclay.common.domain.entity.CustomException.CustomError.CANNOT_CONNECT_TO_HOST
import com.upsaclay.common.domain.entity.CustomException.CustomError.FORBIDDEN
import com.upsaclay.common.domain.entity.CustomException.CustomError.INTERNAL_SERVER
import com.upsaclay.common.domain.entity.CustomException.CustomError.NETWORK
import com.upsaclay.common.domain.entity.CustomException.CustomError.RESOURCE_NOT_FOUND
import com.upsaclay.common.domain.entity.CustomException.CustomError.TIMED_OUT
import com.upsaclay.common.domain.entity.CustomException.CustomError.TOO_MANY_REQUEST
import com.upsaclay.common.domain.entity.CustomException.CustomError.UNAUTHORIZED
import com.upsaclay.common.domain.entity.CustomException.CustomError.UNKNOWN
import java.net.ConnectException
import java.net.SocketTimeoutException

fun mapFirebaseException(exception: Exception): Exception {
    return when (exception) {
        is FirebaseNetworkException -> CustomException(NETWORK, exception)
        is FirebaseTooManyRequestsException -> CustomException(TOO_MANY_REQUEST, exception)
        is FirebaseFirestoreException -> {
            when (exception.code) {
                FirebaseFirestoreException.Code.UNKNOWN -> CustomException(UNKNOWN, exception)
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> CustomException(FORBIDDEN, exception)
                FirebaseFirestoreException.Code.RESOURCE_EXHAUSTED -> CustomException(TOO_MANY_REQUEST, exception)
                FirebaseFirestoreException.Code.UNAUTHENTICATED -> CustomException(UNAUTHORIZED, exception)
                else -> CustomException(UNKNOWN, exception)
            }
        }
        else -> exception
    }
}

fun mapServerException(exception: Exception): Exception {
    return when (exception) {
        is ConnectException -> CustomException(CANNOT_CONNECT_TO_HOST, exception)
        is SocketTimeoutException -> CustomException(TIMED_OUT, exception)
        is ServerException -> {
            when (exception.httpCode) {
                400 -> CustomException(BAD_REQUEST, exception)
                401 -> CustomException(UNAUTHORIZED, exception)
                403 -> CustomException(FORBIDDEN, exception)
                404 -> CustomException(RESOURCE_NOT_FOUND, exception)
                else -> CustomException(INTERNAL_SERVER, exception)
            }
        }
        else -> exception
    }
}
