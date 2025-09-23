package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.BlockUserEvent
import kotlinx.coroutines.flow.Flow

interface BlockedUserRepository {
    val blockUserEvent: Flow<BlockUserEvent>

    val blockedUserIds: Flow<Set<String>>

    suspend fun getLocalBlockedUserIds(): Set<String>

    suspend fun getRemoteBlockedUserIds(currentUserId: String): Set<String>

    suspend fun blockUser(currentUserId: String, blockedUserId: String)

    suspend fun blockLocalUser(blockedUserId: String)

    suspend fun unblockUser(currentUserId: String, blockedUserId: String)

    suspend fun unblockLocalUser(blockedUserId: String)
}