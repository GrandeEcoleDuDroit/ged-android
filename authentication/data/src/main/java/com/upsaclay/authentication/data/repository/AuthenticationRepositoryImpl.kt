package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.model.AuthTokenState
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

internal class AuthenticationRepositoryImpl(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val scope: CoroutineScope
): AuthenticationRepository {
    private val _authenticationState = MutableStateFlow<AuthenticationState?>(null)
    override val authenticationState: Flow<AuthenticationState> = _authenticationState.filterNotNull()
    override var authToken: String? = null
        private set

    init {
        listenAuthenticationState()
        listenAuthTokenState()
    }

    override suspend fun isAuthenticated(): Boolean =
        authenticationLocalDataSource.getAuthenticationState() is AuthenticationState.Authenticated &&
                authenticationRemoteDataSource.isAuthenticated()

    override suspend fun refreshTokenIfNecessary() {
        if (authToken == null) {
            authToken = authenticationRemoteDataSource.getAuthToken()
        }
    }

    override suspend fun loginWithEmailAndPassword(email: String, password: String): String? {
        return try {
            authenticationRemoteDataSource.loginWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            Timber.e("Error logging in user $email: ${e.message}")
            throw e
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String? {
        return try {
            authenticationRemoteDataSource.registerWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            Timber.e("Error registering user $email: ${e.message}")
            throw e
        }
    }

    override suspend fun logout() {
        authenticationRemoteDataSource.logout()
        storeAuthenticationState(AuthenticationState.Unauthenticated)
    }

    override suspend fun storeAuthenticationState(authenticationState: AuthenticationState) {
        authenticationLocalDataSource.storeAuthenticationState(authenticationState)
    }

    override suspend fun resetPassword(email: String) {
        try {
            authenticationRemoteDataSource.resetPassword(email)
        } catch (e: Exception){
            Timber.e("Error resetting password for user $email: ${e.message}")
            throw e
        }
    }

    private fun listenAuthenticationState() {
        scope.launch {
            merge(
                authenticationLocalDataSource.listenAuthenticationState(),
                authenticationRemoteDataSource.listenAuthenticationState().filter { it is AuthenticationState.Unauthenticated }
            ).collect { state ->
                _authenticationState.update { state }
            }
        }
    }

    private fun listenAuthTokenState() {
        scope.launch {
            authenticationRemoteDataSource.listenAuthTokenState().collect { state ->
                when(state) {
                    is AuthTokenState.Valid -> authToken = state.token
                    is AuthTokenState.Unauthenticated -> authToken = null
                    is AuthTokenState.Error -> Timber.e("Error getting auth token: ${state.throwable?.message}")
                }
            }
        }
    }
}