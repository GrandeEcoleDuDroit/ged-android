package com.upsaclay.authentication.data


interface AuthenticationRepository {
    suspend fun loginWithParisSaclay(
        email: String,
        password: String,
        hash: String
    ): Result<AuthenticationState>
}