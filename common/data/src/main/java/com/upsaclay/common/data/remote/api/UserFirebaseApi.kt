package com.upsaclay.common.data.remote.api

internal interface UserFirebaseApi {
    suspend fun getUser(userId: String): RemoteUserFirebase?

    suspend fun getAllUsers(): List<RemoteUserFirebase>

    suspend fun getOnlineUsers(): List<RemoteUserFirebase>

    suspend fun createUser(remoteUserFirebase: RemoteUserFirebase): Result<Unit>

    suspend fun updateProfilePictureUrl(userId: String, profilePictureUrl: String?): Result<Unit>
}