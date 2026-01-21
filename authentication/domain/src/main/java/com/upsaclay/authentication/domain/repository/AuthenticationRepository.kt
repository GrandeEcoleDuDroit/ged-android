package com.upsaclay.authentication.domain.repository

import com.upsaclay.authentication.domain.entity.AuthenticationState
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val authenticationState: Flow<AuthenticationState>

    val currentAuthenticationState: AuthenticationState

    suspend fun isAuthenticated(): Boolean

    suspend fun getAuthToken(): String?

    suspend fun loginWithEmailAndPassword(email: String, password: String): String?

    suspend fun registerWithEmailAndPassword(email: String, password: String): String?

    suspend fun logout()

    suspend fun storeAuthenticationState(authenticationState: AuthenticationState)
}