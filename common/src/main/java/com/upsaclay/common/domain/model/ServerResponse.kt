package com.upsaclay.common.domain.model

sealed class ServerResponse {
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