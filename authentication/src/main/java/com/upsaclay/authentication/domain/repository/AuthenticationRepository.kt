package com.upsaclay.authentication.domain.repository


interface AuthenticationRepository {
    suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<String>

    suspend fun setAuthenticated()

    suspend fun isAuthenticated(): Boolean
}