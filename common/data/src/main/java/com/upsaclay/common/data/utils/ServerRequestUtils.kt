package com.upsaclay.common.data.utils

import com.google.gson.Gson
import com.upsaclay.common.data.exceptions.ServerException
import com.upsaclay.common.data.remote.model.ServerResponse
import retrofit2.Response

suspend fun sendServerRequest(block: suspend () -> Response<ServerResponse>) {
    val response = block()
    if (!response.isSuccessful) {
        val errorBody = response.errorBody()?.string()
        val serverError = errorBody?.let {
            try {
                Gson().fromJson(it, ServerResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }

        throw ServerException(
            httpCode = response.code(),
            message = serverError?.message ?: "Unknown error",
            errorCode = serverError?.code
        )
    }
}

suspend fun <T>sendDataServerRequest(block: suspend () -> Response<T>): T? {
    val response = block()
    return if (response.isSuccessful) {
        response.body()
    } else {
        val errorBody = response.errorBody()?.string()
        val serverError = errorBody?.let {
            try {
                Gson().fromJson(it, ServerResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }

        throw ServerException(
            httpCode = response.code(),
            message = serverError?.message ?: "Unknown error",
            errorCode = serverError?.code
        )
    }
}