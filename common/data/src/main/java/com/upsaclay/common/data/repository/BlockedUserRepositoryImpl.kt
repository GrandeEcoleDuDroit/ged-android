package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.BlockedUserLocalDataSource
import com.upsaclay.common.data.remote.BlockedUserRemoteDataSource
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.repository.BlockedUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.time.LocalDateTime
import java.time.ZoneOffset

internal class BlockedUserRepositoryImpl(
    private val blockedUserLocalDataSource: BlockedUserLocalDataSource,
    private val blockedUserRemoteDataSource: BlockedUserRemoteDataSource
): BlockedUserRepository {
    private val _blockUserEvent = MutableSharedFlow<BlockUserEvent>(replay = 1)
    override val blockUserEvent: Flow<BlockUserEvent> = _blockUserEvent

    override val blockedUserIds: Flow<Set<String>> = blockedUserLocalDataSource.getBlockedUserIdsFlow()

    override suspend fun getLocalBlockedUserIds(): Set<String> = blockedUserLocalDataSource.getBlockedUserIds()

    override suspend fun getRemoteBlockedUserIds(currentUserId: String): Set<String> {
        return try {
            blockedUserRemoteDataSource.getBlockedUsers(currentUserId).map { it.userId }.toSet()
        } catch (e: Exception) {
            e("Error getting remote blocked users for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun blockUser(currentUserId: String, userId: String) {
        try {
            blockedUserRemoteDataSource.addBlockedUser(currentUserId, BlockedUser(userId, LocalDateTime.now(ZoneOffset.UTC)))
            blockedUserLocalDataSource.blockUser(userId)
            _blockUserEvent.emit(BlockUserEvent.Block(userId))
        } catch (e: Exception) {
            e("Error blocking user $userId for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun blockLocalUser(userId: String) {
        blockedUserLocalDataSource.blockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Block(userId))
    }

    override suspend fun unblockUser(currentUserId: String, userId: String) {
        try {
            blockedUserRemoteDataSource.removeBlockedUser(currentUserId, userId)
            blockedUserLocalDataSource.unblockUser(userId)
            _blockUserEvent.emit(BlockUserEvent.Unblock(userId))
        } catch (e: Exception) {
            e("Error unblocking user $userId for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun unblockLocalUser(userId: String) {
        blockedUserLocalDataSource.unblockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Unblock(userId))
    }
}