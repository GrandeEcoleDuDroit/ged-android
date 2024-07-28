package com.upsaclay.core.data.repository

import com.upsaclay.core.data.local.UserLocalDataSource
import com.upsaclay.core.data.remote.UserRemoteDataSource
import com.upsaclay.core.domain.model.User
import com.upsaclay.core.domain.repository.UserRepository
import com.upsaclay.core.utils.errorLog
import com.upsaclay.core.utils.formatHttpError
import com.upsaclay.core.utils.infoLog
import java.io.IOException

internal class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource
): UserRepository {
    override suspend fun createUser(user: User): Result<Int> {
        val response = userRemoteDataSource.createUser(user.toDTO())

        return if (response.isSuccessful) {
            infoLog(response.body()?.message ?: "User created successfully !")
            userLocalDataSource.storeUser(user.toDTO())
            Result.success(response.body()?.data ?: -1)
        } else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            errorLog(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}