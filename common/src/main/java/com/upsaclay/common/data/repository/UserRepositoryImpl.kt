package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.UserLocalDataSource
import com.upsaclay.common.data.remote.UserRemoteDataSource
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.formatHttpError
import com.upsaclay.common.utils.infoLog
import java.io.IOException

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    override suspend fun createUser(user: User): Result<Int> {
        val response = userRemoteDataSource.createUser(user.toDTO())

        return if (response.isSuccessful) {
            infoLog(response.body()?.message ?: "User created successfully !")
            userLocalDataSource.storeCurrentUser(user.toDTO())
            Result.success(response.body()?.data ?: -1)
        } else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            errorLog(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }

    override suspend fun getCurrentUser(): User {
        return userLocalDataSource.getCurrentUser()?.let {
            User.fromDTO(it)
        } ?: throw IllegalStateException("Current user not found")
    }
}