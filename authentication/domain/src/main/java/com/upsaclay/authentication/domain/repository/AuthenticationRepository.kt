package com.upsaclay.authentication.domain.repository

import com.upsaclay.authentication.domain.entity.AuthenticationState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val authenticationState: Flow<AuthenticationState>

    val authToken: String?

    suspend fun isAuthenticated(): Boolean

    suspend fun refreshTokenIfNecessary()

    suspend fun loginWithEmailAndPassword(email: String, password: String): String?

    suspend fun registerWithEmailAndPassword(email: String, password: String): String?

    suspend fun logout()

    suspend fun storeAuthenticationState(authenticationState: AuthenticationState)
    suspend fun forgotPassword(email : String)
}