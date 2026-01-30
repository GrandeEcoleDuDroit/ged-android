package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.BlockedUsers
import kotlinx.coroutines.flow.Flow

interface BlockedUserRepository {
    val blockUserEvent: Flow<BlockUserEvent>
    val blockedUsers: Flow<BlockedUsers>
    val currentBlockedUsers: BlockedUsers?

    suspend fun getLocalBlockedUsers(): BlockedUsers

    suspend fun getRemoteBlockedUsers(currentUserId: String): BlockedUsers

    suspend fun addBlockUser(currentUserId: String, blockedUser: BlockedUser)

    suspend fun addLocalBlockUser(blockedUser: BlockedUser)

    suspend fun removeBlockedUser(currentUserId: String, blockedUserId: String)

    suspend fun removeLocalBlockedUser(blockedUserId: String)

    suspend fun deleteLocalBlockedUsers()
}