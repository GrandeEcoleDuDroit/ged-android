package com.upsaclay.common.data.remote.model

data class ServerResponse(
    val message: String,
    val code: String?,
    val error: String?
)