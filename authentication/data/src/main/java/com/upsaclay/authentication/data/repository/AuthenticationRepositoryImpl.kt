package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.data.utils.w
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class AuthenticationRepositoryImpl(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource
) : AuthenticationRepository {
    override suspend fun isAuthenticated(): Boolean =
        authenticationLocalDataSource.isAuthenticated() && authenticationRemoteDataSource.isAuthenticated()

    override fun getAuthenticationState(): Flow<Boolean> = combine(
        authenticationLocalDataSource.listenAuthenticationState(),
        authenticationRemoteDataSource.listenAuthenticationState(),
    ) { localAuthState, firebaseAuthState ->
        localAuthState && firebaseAuthState
    }

    override fun getAuthToken(): String? = authenticationRemoteDataSource.getAuthToken()

    override suspend fun loginWithEmailAndPassword(email: String, password: String): String? {
        return try {
            authenticationRemoteDataSource.loginWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            w("Error logging in user $email: ${e.message}")
            throw e
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String {
        return try {
            authenticationRemoteDataSource.registerWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            w("Error registering user $email: ${e.message}")
            throw e
        }
    }

    override suspend fun logout() {
        authenticationRemoteDataSource.logout()
        setAuthenticated(false)
    }

    override suspend fun setAuthenticated(isAuthenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(isAuthenticated)
    }

    override suspend fun deleteAuthUser() {
        authenticationRemoteDataSource.deleteAuthUser()
        setAuthenticated(false)
    }
}