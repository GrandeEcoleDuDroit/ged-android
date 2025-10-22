package com.upsaclay.common.data.remote.api

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.flow.Flow

internal interface UserApi {
    suspend fun getUser(userId: String): User?

    fun getUserFlow(userId: String): Flow<User?>

    suspend fun getUserWithEmail(userEmail: String): User?

    suspend fun getUsers(): List<User>

    suspend fun getMemberUsers(): List<User>

    suspend fun createUser(user: User)

    suspend fun updateUser(user: User)

    suspend fun updateProfilePictureFileName(userId: String, fileName: String)

    suspend fun deleteProfilePictureFileName(userId: String)

    suspend fun reportUser(report: UserReport)
}