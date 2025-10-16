package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val user: Flow<User>
    val currentUser: User?

    suspend fun getUsers(): List<User>

    suspend fun getUser(userId: String): User?

    suspend fun getCurrentUser(): User?

    fun getUserFlow(userId: String): Flow<User?>

    suspend fun getUserWithEmail(userEmail: String): User?

    suspend fun createUser(user: User)

    suspend fun storeUser(user: User)

    suspend fun updateRemoteUser(user: User)

    suspend fun updateProfilePictureFileName(userId: String, fileName: String)

    suspend fun deleteLocalUser()

    suspend fun deleteProfilePictureFileName(userId: String)

    suspend fun reportUser(report: UserReport)
}