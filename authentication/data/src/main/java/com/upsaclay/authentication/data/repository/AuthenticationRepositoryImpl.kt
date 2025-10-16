package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.repository.firebase.FirebaseAuthenticationRepository
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class AuthenticationRepositoryImpl(
    private val firebaseAuthenticationRepository: FirebaseAuthenticationRepository,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
) : AuthenticationRepository {
    override suspend fun isAuthenticated(): Boolean =
        authenticationLocalDataSource.isAuthenticated() && firebaseAuthenticationRepository.isAuthenticated()

    override fun getAuthenticationState(): Flow<Boolean> = combine(
        authenticationLocalDataSource.listenAuthenticationState(),
        firebaseAuthenticationRepository.listenAuthenticationState(),
    ) { localAuthState, firebaseAuthState ->
        localAuthState && firebaseAuthState
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String) {
        firebaseAuthenticationRepository.loginWithEmailAndPassword(email, password)
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String =
        firebaseAuthenticationRepository.registerWithEmailAndPassword(email, password)

    override suspend fun logout() {
        firebaseAuthenticationRepository.logout()
        setAuthenticated(false)
    }

    override suspend fun setAuthenticated(isAuthenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(isAuthenticated)
    }

    override suspend fun deleteAuthUser() {
        firebaseAuthenticationRepository.deleteAuthUser()
        setAuthenticated(false)
    }
}