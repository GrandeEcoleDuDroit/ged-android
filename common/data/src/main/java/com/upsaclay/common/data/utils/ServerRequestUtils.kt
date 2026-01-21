package com.upsaclay.common.data.utils

import com.google.gson.Gson
import com.upsaclay.common.data.exceptions.ServerException
import com.upsaclay.common.data.remote.model.ServerResponse
import retrofit2.Response

suspend fun sendServerRequest(block: suspend () -> Response<ServerResponse>) {
    val response = block()
    if (!response.isSuccessful) {
        throw formatServerException(response)
    }
}

suspend fun <T> sendDataServerRequest(block: suspend () -> Response<T>): T? {
    val response = block()
    return if (response.isSuccessful) {
        response.body()
    } else {
        throw formatServerException(response)
    }
}

private fun <T> formatServerException(response: Response<T>): ServerException {
    val errorBody = response.errorBody()?.string()
    val serverError = errorBody?.let {
        runCatching { Gson().fromJson(it, ServerResponse::class.java) }.getOrNull()
    }

    return ServerException(
        httpCode = response.code(),
        message = serverError?.message ?: "Unknown error",
        errorCode = serverError?.code
    )
}