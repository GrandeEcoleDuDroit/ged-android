package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val currentUser: StateFlow<User?>

    suspend fun createUser(user: User): Int?

    suspend fun getUser(userId: Int): User?

    suspend fun getAllUsers(): List<User>

    suspend fun getOnlineUsers(): List<User>

    suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit>

    suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit>
}