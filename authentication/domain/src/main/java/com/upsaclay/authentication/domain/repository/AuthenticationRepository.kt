package com.upsaclay.authentication.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    val authenticationState: Flow<Boolean>

    val currentAuthenticationState: Boolean

    fun getAuthToken(): String?

    suspend fun loginWithEmailAndPassword(email: String, password: String): String?

    suspend fun registerWithEmailAndPassword(email: String, password: String): String?

    suspend fun logout()

    suspend fun setAuthenticated(isAuthenticated: Boolean)

    suspend fun deleteAuthUser()
}