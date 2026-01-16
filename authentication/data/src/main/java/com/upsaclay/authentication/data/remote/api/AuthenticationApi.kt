package com.upsaclay.authentication.data.remote.api

import kotlinx.coroutines.flow.Flow

interface AuthenticationApi {
    fun isAuthenticated(): Boolean

    fun listenAuthenticationState(): Flow<Boolean>

    fun getIdToken(): String?

    suspend fun signIn(email: String, password: String): String?

    suspend fun signUp(email: String, password: String): String

    fun signOut()

    suspend fun deleteAuthUser()
}