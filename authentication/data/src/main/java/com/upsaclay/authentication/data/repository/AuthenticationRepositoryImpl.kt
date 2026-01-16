package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.model.AuthTokenState
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

internal class AuthenticationRepositoryImpl(
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val scope: CoroutineScope
) : AuthenticationRepository {
    private var authToken: String? = null
    private val _authenticationState = MutableStateFlow<Boolean?>(null)
    override val authenticationState: Flow<Boolean> = _authenticationState.filterNotNull()
    override val currentAuthenticationState: Boolean
        get() = _authenticationState.value ?: false

    init {
        listenAuthenticationState()
        listenAuthTokenState()
    }

    override fun getAuthToken(): String? = authToken

    override suspend fun loginWithEmailAndPassword(email: String, password: String): String? {
        return try {
            authenticationRemoteDataSource.loginWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            Timber.e("Error logging in user $email", e)
            throw e
        }
    }

    override suspend fun registerWithEmailAndPassword(email: String, password: String): String? {
        return try {
            authenticationRemoteDataSource.registerWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            Timber.e("Error registering user $email", e)
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

    private fun listenAuthenticationState() {
        scope.launch {
            combine(
                authenticationLocalDataSource.listenAuthenticationState(),
                authenticationRemoteDataSource.listenAuthenticationState()
            ) { local, remote ->
                local && remote
            }.collect {
                _authenticationState.value = it
            }
        }
    }

    private fun listenAuthTokenState() {
        scope.launch {
            authenticationRemoteDataSource.listenAuthTokenState().collect { state ->
                when(state) {
                    is AuthTokenState.Valid -> authToken = state.token
                    is AuthTokenState.Unauthenticated -> {
                        authToken = null
                        setAuthenticated(false)
                    }
                    is AuthTokenState.Error -> {
                        Timber.e("Error getting auth token", state.throwable)
                    }
                }
            }
        }
    }
}