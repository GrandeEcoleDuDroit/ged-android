package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val currentUserFlow: Flow<User>
    val currentUser: User?

    suspend fun getUserWithFirestore(userId: Int): User?

    suspend fun getUserWithFirestore(userEmail: String): User?

    suspend fun getUserWithOracle(email: String): User?

    suspend fun getAllUsers(): List<User>

    suspend fun getOnlineUsers(): List<User>

    suspend fun createUserWithOracle(user: User): Int?

    suspend fun createUserWithFirestore(user: User): Result<Unit>

    suspend fun setCurrentUser(user: User)

    suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit>

    suspend fun deleteProfilePictureUrl(userId: Int): Result<Unit>

    suspend fun isUserExist(email: String): Result<Boolean>
}