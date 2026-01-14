package com.upsaclay.common.data.remote.api.user

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.flow.Flow
import java.io.File

internal interface UserApi {
    suspend fun getUser(userId: String): User?

    fun listenUser(userId: String): Flow<User?>

    suspend fun getUsers(): List<User>

    suspend fun createUser(user: User)

    suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String)

    suspend fun deleteProfilePicture(user: User)

    suspend fun reportUser(report: UserReport)
}