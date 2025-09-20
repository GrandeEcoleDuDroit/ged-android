package com.upsaclay.common.data.local

import com.upsaclay.common.data.local.datastore.BlockedUserDataStore

internal class BlockedUserLocalDataSource(
    private val blockedUserDataStore: BlockedUserDataStore
) {
    suspend fun getBlockedUserIds(): Set<String> = blockedUserDataStore.getBlockedUserIds()

    suspend fun blockUser(userId: String) {
        blockedUserDataStore.blockUser(userId)
    }

    suspend fun unblockUser(userId: String) {
        blockedUserDataStore.unblockUser(userId)
    }
}