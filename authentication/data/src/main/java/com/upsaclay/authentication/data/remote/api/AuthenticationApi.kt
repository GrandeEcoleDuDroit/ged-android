package com.upsaclay.authentication.data.remote.api

import com.upsaclay.authentication.data.model.AuthTokenState
import com.upsaclay.authentication.domain.entity.AuthenticationState
import kotlinx.coroutines.flow.Flow

interface AuthenticationApi {
    fun listenAuthenticationState(): Flow<AuthenticationState>

    fun listenAuthTokenState(): Flow<AuthTokenState>

    fun isAuthenticated(): Boolean

    suspend fun getAuthToken(): String?

    suspend fun signIn(email: String, password: String): String?

    suspend fun signUp(email: String, password: String): String?

    fun signOut()
    fun forgotPassword(email: String)
}