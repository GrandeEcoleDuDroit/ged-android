package com.upsaclay.common.data.exceptions

data class ServerException(
    val httpCode: Int,
    override val message: String?,
    val errorCode: String? = null
): Exception()