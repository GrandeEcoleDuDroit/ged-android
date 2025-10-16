package com.upsaclay.authentication.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun isAuthenticated(): Boolean

    fun getAuthenticationState(): Flow<Boolean>

    suspend fun loginWithEmailAndPassword(email: String, password: String)

    suspend fun registerWithEmailAndPassword(email: String, password: String): String

    suspend fun logout()

    suspend fun setAuthenticated(isAuthenticated: Boolean)

    suspend fun deleteAuthUser()
}