package com.upsaclay.authentication.data.remote

data class RemoteUser(
    val username: String,
    val password: String,
    val hash: String
)