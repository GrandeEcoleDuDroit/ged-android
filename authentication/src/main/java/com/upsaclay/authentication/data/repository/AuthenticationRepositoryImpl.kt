package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.infoLog
import java.io.IOException

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
) : AuthenticationRepository {
    override suspend fun loginWithParisSaclay(email: String, password: String, hash: String): Result<String> {
        val loginResponse = authenticationRemoteDataSource.loginWithParisSaclay(email, password, hash)

        return if (loginResponse.isSuccessful) {
            infoLog("Login successfully !")
            Result.success(loginResponse.body()?.message ?: "")
        } else {
            val errorMessage = formatHttpError(loginResponse.message(), loginResponse.errorBody()?.string())
            Result.failure(IOException("Error authentication request : $errorMessage"))
        }
    }

    override suspend fun setAuthenticated() {
        authenticationLocalDataSource.setAuthenticated()
    }

    override suspend fun isAuthenticated(): Boolean = authenticationLocalDataSource.isAuthenticated()
}