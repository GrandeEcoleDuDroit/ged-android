package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.infoLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
): AuthenticationRepository {
    private val _isAuthenticated = MutableStateFlow(false)
    override val isAuthenticatedFlow: Flow<Boolean> = _isAuthenticated.asStateFlow()
    override val isAuthenticated: Boolean
        get() = _isAuthenticated.value

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _isAuthenticated.value = authenticationLocalDataSource.isAuthenticated()
        }
    }

    override suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<String> {
        val loginResponse = authenticationRemoteDataSource.loginWithParisSaclay(email, password, hash)

        return if (loginResponse.isSuccessful) {
            infoLog("Login successfully !")
            updateAuthenticationState(true)
            Result.success(loginResponse.body()?.message ?: "")
        } else {
            val errorMessage = formatHttpError(loginResponse.message(), loginResponse.errorBody()?.string())
            Result.failure(IOException("Error authentication request : $errorMessage"))
        }
    }

    override suspend fun logout() {
        updateAuthenticationState(false)
    }

    private suspend fun updateAuthenticationState(authenticated: Boolean) {
        if (authenticated) {
            authenticationLocalDataSource.setAuthenticated()
            _isAuthenticated.value = true
        }
        else {
            authenticationLocalDataSource.setUnauthenticated()
            _isAuthenticated.value = false
        }
    }
}