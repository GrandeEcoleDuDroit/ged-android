package com.upsaclay.authentication.data.repository

import com.upsaclay.authentication.data.local.AuthenticationLocalDataSource
import com.upsaclay.authentication.data.remote.AuthenticationRemoteDataSource
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.core.domain.model.User
import com.upsaclay.core.domain.repository.UserRepository
import com.upsaclay.core.utils.formatHttpError
import com.upsaclay.core.utils.infoLog
import java.io.IOException

class AuthenticationRepositoryImpl(
    private val authenticationRemoteDataSource: AuthenticationRemoteDataSource,
    private val authenticationLocalDataSource: AuthenticationLocalDataSource,
    private val userRepository: UserRepository
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

    override suspend fun register(user: User): Result<Int> {
        return userRepository.createUser(user).also {
            infoLog("Registration successfully !")
            if (it.isSuccess) {
                authenticationLocalDataSource.setAuthenticated()
            }
            else {
                infoLog("Registration failed")
            }
        }
    }

    override suspend fun isAuthenticated(): Boolean = authenticationLocalDataSource.isAuthenticated()
}