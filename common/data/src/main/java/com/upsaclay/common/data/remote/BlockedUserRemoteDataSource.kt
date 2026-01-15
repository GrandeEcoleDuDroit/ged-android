package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.BlockedUserApi
import com.upsaclay.common.data.toBlockedUser
import com.upsaclay.common.data.toRemote
import com.upsaclay.common.domain.entity.BlockedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class BlockedUserRemoteDataSource(
    private val blockedUserApi: BlockedUserApi
) {
    suspend fun getBlockedUsers(currentUserId: String): List<BlockedUser> {
        return withContext(Dispatchers.IO) {
            blockedUserApi.getBlockedUsers(currentUserId)?.map { it.toBlockedUser() } ?: emptyList()
        }
    }

    suspend fun addBlockedUser(currentUserId: String, blockedUser: BlockedUser) {
        withContext(Dispatchers.IO) {
            blockedUserApi.blockUser(blockedUser.toRemote(currentUserId))
        }
    }

    suspend fun removeBlockedUser(currentUserId: String, blockedUserId: String) {
        withContext(Dispatchers.IO) {
            blockedUserApi.unblockUser(currentUserId, blockedUserId)
        }
    }
}