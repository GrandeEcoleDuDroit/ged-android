package com.upsaclay.common.data.local

import com.upsaclay.common.data.local.datastore.BlockedUserDataStore
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.BlockedUsers
import kotlinx.coroutines.flow.Flow

internal class BlockedUserLocalDataSource(
    private val blockedUserDataStore: BlockedUserDataStore
) {
    fun getBlockedUsersFlow(): Flow<BlockedUsers> = blockedUserDataStore.getBlockedUsersFlow()

    suspend fun getBlockedUsers(): BlockedUsers = blockedUserDataStore.getBlockedUsers()

    suspend fun addBlockUser(blockedUser: BlockedUser) {
        blockedUserDataStore.addBlockedUser(blockedUser)
    }

    suspend fun removeBlockUser(userId: String) {
        blockedUserDataStore.removeBlockUser(userId)
    }

    suspend fun deleteBlockedUsers() {
        blockedUserDataStore.deleteBlockedUsers()
    }
}