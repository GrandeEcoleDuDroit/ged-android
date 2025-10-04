package com.upsaclay.authentication.data.api

import kotlinx.coroutines.flow.Flow

interface FirebaseAuthenticationApi {
    fun isAuthenticated(): Boolean

    fun listenAuthenticationState(): Flow<Boolean>

    fun getIdToken(): String?

    suspend fun signIn(email: String, password: String)

    suspend fun signUp(email: String, password: String): String

    fun signOut()
}