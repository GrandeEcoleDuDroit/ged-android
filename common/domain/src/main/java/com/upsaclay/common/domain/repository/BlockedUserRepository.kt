package com.upsaclay.common.domain.repository

import com.upsaclay.common.domain.entity.BlockUserEvent
import kotlinx.coroutines.flow.Flow

interface BlockedUserRepository {
    val blockUserEvent: Flow<BlockUserEvent>

    suspend fun getBlockedUserIds(): Set<String>

    suspend fun blockUser(currentUserId: String, blockedUserId: String)

    suspend fun unblockUser(currentUserId: String, blockedUserId: String)
}