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

        return if (response.isSuccessful && response.body()?.data != null) {
            infoLog(response.body()?.message ?: "User created successfully !")
            val userId = response.body()!!.data
            userLocalDataSource.createCurrentUser(user.copy(id = userId).toDTO())
            Result.success(userId)
        }
        else {
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

    override suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit> {
        val response = userRemoteDataSource.updateProfilePictureUrl(userId, profilePictureUrl)
        return if (response.isSuccessful) {
            infoLog("Profile picture updated successfully !")
            userLocalDataSource.updateProfilePictureUrl(profilePictureUrl)
            Result.success(Unit)
        }
        else {
            val errorMessage = formatHttpError(response.message(), response.errorBody()?.string())
            errorLog(errorMessage)
            Result.failure(IOException(errorMessage))
        }
    }
}