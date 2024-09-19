package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUserFlow: Flow<User>
    val currentUser: User?

    suspend fun getAllUser(): List<User>

    suspend fun getUser(userId: Int): User?

    suspend fun createUser(user: User): Result<Int>

    suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit>

    suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit>
}