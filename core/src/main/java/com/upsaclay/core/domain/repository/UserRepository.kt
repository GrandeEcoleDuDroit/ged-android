package com.upsaclay.core.domain.repository

import com.upsaclay.core.domain.model.User

interface UserRepository {
    suspend fun createUser(user: User): Result<Int>
}