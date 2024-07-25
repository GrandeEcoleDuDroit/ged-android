package com.upsaclay.authentication.data

import com.upsaclay.authentication.data.model.AuthenticationState
import com.upsaclay.core.data.model.User


interface AuthenticationRepository {
    suspend fun login(email: String, password: String): Result<AuthenticationState>
    suspend fun createUser(user: User): Result<Int>
}