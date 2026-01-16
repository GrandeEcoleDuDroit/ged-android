package com.upsaclay.authentication.data.remote.api

import com.upsaclay.authentication.data.model.AuthTokenState
import kotlinx.coroutines.flow.Flow

interface AuthenticationApi {
    fun listenAuthenticationState(): Flow<Boolean>

    fun listenAuthTokenState(): Flow<AuthTokenState>

    suspend fun signIn(email: String, password: String): String?

    suspend fun signUp(email: String, password: String): String?

    fun signOut()

    suspend fun deleteAuthUser()
}