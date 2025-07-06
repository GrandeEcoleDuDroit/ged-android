package com.upsaclay.authentication.data.repository.firebase

import kotlinx.coroutines.flow.Flow

interface FirebaseAuthenticationRepository {
    fun listenAuthenticationState(): Flow<Boolean>

    fun getIdToken(): String?

    suspend fun loginWithEmailAndPassword(email: String, password: String)

    suspend fun registerWithEmailAndPassword(email: String, password: String): String

    fun logout()
}