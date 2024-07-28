package com.upsaclay.authentication.domain.repository

import com.upsaclay.core.domain.model.User


interface AuthenticationRepository {
    suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<String>

    suspend fun register(user: User): Result<Int>

    suspend fun isAuthenticated(): Boolean
}