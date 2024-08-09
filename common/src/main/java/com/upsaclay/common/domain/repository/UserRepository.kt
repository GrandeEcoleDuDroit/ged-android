package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User>
    val user: User?

    suspend fun createUser(user: User): Result<Int>

    suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit>
}