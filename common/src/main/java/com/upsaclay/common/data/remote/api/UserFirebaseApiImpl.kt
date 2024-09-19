package com.upsaclay.common.data.remote.api

import com.upsaclay.common.domain.model.User

class UserFirebaseApiImpl: UserFirebaseApi {
    override suspend fun getUser(userId: Int): Result<User?> {
        TODO("Not yet implemented")
    }

    override suspend fun createUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }
}