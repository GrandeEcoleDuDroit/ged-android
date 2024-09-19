package com.upsaclay.common.data.remote.api

import com.upsaclay.common.domain.model.User

internal interface UserFirebaseApi {
    suspend fun getUser(userId: Int): Result<User?>

    suspend fun createUser(user: User): Result<Unit>

    suspend fun updateUser(user: User): Result<Unit>
}