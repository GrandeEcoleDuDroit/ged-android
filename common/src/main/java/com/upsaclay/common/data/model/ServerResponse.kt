package com.upsaclay.common.data.model

sealed class ServerResponse {
    data class StringResponse(
        val message: String,
        val data: String?,
        val error: String?
    ) : ServerResponse()

    data class IntResponse(
        val message: String,
        val data: Int?,
        val error: String?
    ): ServerResponse()

    data class EmptyResponse(
        val message: String,
        val error: String?
    ): ServerResponse()
}