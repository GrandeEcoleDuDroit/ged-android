package com.upsaclay.authentication.data.repository

import android.credentials.GetCredentialException
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.d
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

internal class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
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

    override suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<Unit> {
        return authenticationRemoteDataSource.loginWithParisSaclay(email, password, hash)
    }

    override suspend fun setAuthenticated(isAuthenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(isAuthenticated)
    }

    override suspend fun logout() {
        authenticationLocalDataSource.setAuthenticationState(false)
    }
}