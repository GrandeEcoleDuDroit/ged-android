package com.upsaclay.common.data.remote.api

interface BlockedUserApi {
    suspend fun blockUser(currentUserId: String, blockedUserId: String)

    suspend fun unblockUser(currentUserId: String, blockedUserId: String)
}