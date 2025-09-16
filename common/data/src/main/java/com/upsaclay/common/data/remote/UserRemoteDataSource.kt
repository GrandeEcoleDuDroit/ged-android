package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.UserApi
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class UserRemoteDataSource(
    private val userApi: UserApi
) {
    suspend fun getUser(userId: String): User? = withContext(Dispatchers.IO) {
        userApi.getUser(userId)
    }

    fun getUserFlow(userId: String): Flow<User?> = userApi.getUserFlow(userId)

    suspend fun getUserFirestoreWithEmail(userEmail: String): User? = withContext(Dispatchers.IO) {
        userApi.getUserWithEmail(userEmail)
    }

    suspend fun getUsers(): List<User> = withContext(Dispatchers.IO) {
        userApi.getUsers()
    }

    suspend fun createUser(user: User) {
        withContext(Dispatchers.IO) {
            userApi.createUser(user)
        }
    }

    suspend fun updateProfilePictureFileName(userId: String, fileName: String) {
        withContext(Dispatchers.IO) {
            userApi.updateProfilePictureFileName(userId, fileName)
        }
    }

    suspend fun deleteProfilePictureFileName(userId: String) {
        withContext(Dispatchers.IO) {
            userApi.deleteProfilePictureFileName(userId)
        }
    }

    suspend fun isUserExist(email: String): Boolean = withContext(Dispatchers.IO) {
        userApi.isUserExist(email)
    }

    suspend fun reportUser(report: UserReport) {
        withContext(Dispatchers.IO) {
            userApi.reportUser(report)
        }
    }
}