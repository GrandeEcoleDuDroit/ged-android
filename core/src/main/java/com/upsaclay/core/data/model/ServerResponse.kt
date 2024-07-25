package com.upsaclay.core.data.model

sealed class ServerResponse {
    data class StringServerResponse(
        val message: String,
        val data: String,
        val error: String?
    ) : ServerResponse()

    data class IntServerResponse(
        val message: String,
        val data: Int,
        val error: String?
    ): ServerResponse()

    data class EmptyServerResponse(
        val message: String,
        val error: String?
    ): ServerResponse()
}