package com.upsaclay.authentication.domain.repository

import kotlinx.coroutines.flow.Flow


interface AuthenticationRepository {
    val isAuthenticatedFlow: Flow<Boolean>
    val isAuthenticated: Boolean

    suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<String>

    suspend fun logout()
}