package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.BlockUserEvent
import kotlinx.coroutines.flow.Flow

interface BlockedUserRepository {
    val blockUserEvent: Flow<BlockUserEvent>

    val blockedUserIds: Flow<Set<String>>

    suspend fun getLocalBlockedUserIds(): Set<String>

    suspend fun getRemoteBlockedUserIds(currentUserId: String): Set<String>

    suspend fun blockUser(currentUserId: String, userId: String)

    suspend fun blockLocalUser(userId: String)

    suspend fun unblockUser(currentUserId: String, userId: String)

    suspend fun unblockLocalUser(userId: String)
}