package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    val user: Flow<User>
    val getLocalUser: User?

    fun getUserFlow(userId: String): Flow<User?>

    suspend fun getUsers(): List<User>

    suspend fun getUser(userId: String): User?

    suspend fun getCurrentUser(): User?

    suspend fun createUser(user: User)

    suspend fun storeUser(user: User)

    suspend fun updateProfilePicture(user: User, imageFile: File, fileName: String)

    suspend fun deleteUser(user: User)

    suspend fun deleteLocalUser()

    suspend fun deleteProfilePicture(user: User)

    suspend fun reportUser(report: UserReport)
}