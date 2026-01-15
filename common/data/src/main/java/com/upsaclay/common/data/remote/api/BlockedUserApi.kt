package com.upsaclay.common.data.remote.api

import com.upsaclay.common.data.remote.model.RemoteBlockedUser

internal interface BlockedUserApi {
    suspend fun getBlockedUsers(currentUserId: String): List<RemoteBlockedUser>?

    suspend fun blockUser(remoteBlockedUser: RemoteBlockedUser)

    suspend fun unblockUser(currentUserId: String, blockedUserId: String)
}