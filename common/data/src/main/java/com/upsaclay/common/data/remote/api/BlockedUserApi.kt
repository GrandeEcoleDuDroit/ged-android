package com.upsaclay.common.data.remote.api

internal interface BlockedUserApi {
    suspend fun getBlockedUserIds(currentUserId: String): Set<String>

    suspend fun blockUser(currentUserId: String, userId: String)

    suspend fun unblockUser(currentUserId: String, userId: String)
}