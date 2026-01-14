package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.user.UserApi
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File

internal class UserRemoteDataSource(private val userApi: UserApi) {
    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        userApi.getUser(userId)
    }

    fun listenUser(userId: String): Flow<User?> = userApi.listenUser(userId)

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        userApi.getUsers()
    }

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            userApi.createUser(user)
        }
    }

    suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String) {
        withContext(Dispatchers.IO) {
            userApi.updateProfilePicture(user, imageFile, fileName)
        }
    }

    suspend fun deleteProfilePicture(user: User) {
        withContext(Dispatchers.IO) {
            userApi.deleteProfilePicture(user)
        }
    }

    suspend fun reportUser(report: UserReport) {
        withContext(Dispatchers.IO) {
            userApi.reportUser(report)
        }
    }
}