package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
) : AuthenticationRepository {
    private val _isAuthenticated = MutableStateFlow(false)
    override val isAuthenticated: Flow<Boolean> = _isAuthenticated

    init {
        CoroutineScope(Dispatchers.IO).launch {
            authenticationLocalDataSource.getAuthenticationState().collect {
                _isAuthenticated.value = it
            }
        }
    }

    override suspend fun login(email: String, password: String, hash: String): Result<Unit> =
        authenticationRemoteDataSource.login(email, password, hash)

    override suspend fun logout() {
        authenticationLocalDataSource.setAuthenticationState(false)
    }
}