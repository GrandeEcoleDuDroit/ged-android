package com.upsaclay.common.data.remote

import com.upsaclay.common.data.remote.api.BlockedUserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BlockedUserRemoteDataSource(
    private val blockedUserApi: BlockedUserApi
) {
    suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        withContext(Dispatchers.IO) {
            blockedUserApi.blockUser(currentUserId, blockedUserId)
        }
    }

    suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        withContext(Dispatchers.IO) {
            blockedUserApi.unblockUser(currentUserId, blockedUserId)
        }
    }
}