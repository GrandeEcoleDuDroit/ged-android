package com.upsaclay.common.data.utils

import com.upsaclay.common.data.exceptions.ServerException
import com.upsaclay.common.data.remote.model.ServerResponse
import retrofit2.Response

suspend fun sendServerRequest(block: suspend () -> Response<ServerResponse>) {
    val response = block()
    if (!response.isSuccessful) {
        throw ServerException(
            httpCode = response.code(),
            message = response.body()?.message,
            errorCode = response.body()?.code
        )
    }
}

suspend fun <T>sendDataServerRequest(block: suspend () -> Response<T>): T? {
    val response = block()
    return if (response.isSuccessful) {
        response.body()
    } else {
        val serverResponse = response.body() as? ServerResponse
        throw ServerException(
            httpCode = response.code(),
            message = serverResponse?.message,
            errorCode = serverResponse?.code
        )
    }
}