package com.upsaclay.common.data.remote.api

internal interface UserFirebaseApi {
    suspend fun getUser(userId: String): FirebaseUserModel?

    suspend fun getAllUsers(): List<FirebaseUserModel>

    suspend fun getOnlineUsers(): List<FirebaseUserModel>

    suspend fun createUser(firebaseUserModel: FirebaseUserModel): Result<Unit>

    suspend fun updateProfilePictureUrl(userId: String, profilePictureUrl: String?): Result<Unit>
}