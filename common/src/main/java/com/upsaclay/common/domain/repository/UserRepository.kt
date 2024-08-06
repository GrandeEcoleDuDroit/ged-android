package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User): Result<Int>

    suspend fun getCurrentUser(): User

    suspend fun updateProfilePictureUrl(userId: Int, profilePictureUrl: String): Result<Unit>
}