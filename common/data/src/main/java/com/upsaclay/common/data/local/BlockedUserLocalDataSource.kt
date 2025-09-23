package com.upsaclay.common.data.local

import com.upsaclay.common.data.local.datastore.BlockedUserDataStore
import kotlinx.coroutines.flow.Flow

internal class BlockedUserLocalDataSource(
    private val blockedUserDataStore: BlockedUserDataStore
) {
    fun getBlockedUserIdsFlow(): Flow<Set<String>> = blockedUserDataStore.getBlockedUserIdsFlow()

    suspend fun getBlockedUserIds(): Set<String> = blockedUserDataStore.getBlockedUserIds()

    suspend fun blockUser(userId: String) {
        blockedUserDataStore.blockUser(userId)
    }

    suspend fun unblockUser(userId: String) {
        blockedUserDataStore.unblockUser(userId)
    }
}