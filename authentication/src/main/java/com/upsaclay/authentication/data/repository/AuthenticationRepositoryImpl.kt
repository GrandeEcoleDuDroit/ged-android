package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.utils.e
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.i
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

internal class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource
): AuthenticationRepository {
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
        val loginResponse = authenticationRemoteDataSource.loginWithParisSaclay(email, password, hash)

        return if (loginResponse.isSuccessful) {
            i("Login successfully !")
            updateAuthenticationState(true)
            Result.success(Unit)
        } else {
            val errorMessage = formatHttpError(loginResponse.message(), loginResponse.errorBody()?.string())
            e(errorMessage)
            Result.failure(IOException("Error authentication request : $errorMessage"))
        }
    }

    override suspend fun logout() {
        updateAuthenticationState(false)
    }

    private suspend fun updateAuthenticationState(authenticated: Boolean) {
        authenticationLocalDataSource.setAuthenticationState(authenticated)
    }
}