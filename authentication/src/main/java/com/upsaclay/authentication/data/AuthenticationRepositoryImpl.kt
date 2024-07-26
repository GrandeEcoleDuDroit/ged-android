package com.upsaclay.authentication.data

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.model.AuthenticationState
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.core.data.model.User
import com.upsaclay.core.utils.formatHttpError
import com.upsaclay.core.utils.infoLog
import java.io.IOException

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
) : AuthenticationRepository {

    override suspend fun login(email: String, password: String): Result<AuthenticationState> {
        val response = authenticationRemoteDataSource.login(email, password)

        return if (response.isSuccessful) {
            infoLog("Login successful !")
            Result.success(AuthenticationState.AUTHENTICATED)
        } else Result.failure(IOException("Error authentication request : $response"))
    }

    override suspend fun createUser(user: User): Result<Int> {
        val response = authenticationRemoteDataSource.createUser(user.toDTO())
        return if (response.isSuccessful) {
            infoLog(response.body()?.message ?: "User created successfully")
            Result.success(response.body()?.data ?: -1)
        } else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            Result.failure(IOException(errorMessage))
        }
    }
}