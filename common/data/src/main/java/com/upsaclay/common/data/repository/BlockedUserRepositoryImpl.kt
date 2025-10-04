package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.BlockedUserLocalDataSource
import com.upsaclay.common.data.remote.BlockedUserRemoteDataSource
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class BlockedUserRepositoryImpl(
    private val blockedUserLocalDataSource: BlockedUserLocalDataSource,
    private val blockedUserRemoteDataSource: BlockedUserRemoteDataSource
): BlockedUserRepository {
    private val _blockUserEvent = MutableSharedFlow<BlockUserEvent>(replay = 1)
    override val blockUserEvent: Flow<BlockUserEvent> = _blockUserEvent

    override val blockedUserIds: Flow<Set<String>> = blockedUserLocalDataSource.getBlockedUserIdsFlow()

    override suspend fun getLocalBlockedUserIds(): Set<String> = blockedUserLocalDataSource.getBlockedUserIds()

    override suspend fun getRemoteBlockedUserIds(currentUserId: String): Set<String> =
        blockedUserRemoteDataSource.getBlockedUserIds(currentUserId)

    override suspend fun blockUser(currentUserId: String, userId: String) {
        blockedUserRemoteDataSource.blockUser(currentUserId, userId)
        blockedUserLocalDataSource.blockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Block(userId))
    }

    override suspend fun blockLocalUser(userId: String) {
        blockedUserLocalDataSource.blockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Block(userId))
    }

    override suspend fun unblockUser(currentUserId: String, userId: String) {
        blockedUserRemoteDataSource.unblockUser(currentUserId, userId)
        blockedUserLocalDataSource.unblockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Unblock(userId))
    }

    override suspend fun unblockLocalUser(userId: String) {
        blockedUserLocalDataSource.unblockUser(userId)
        _blockUserEvent.emit(BlockUserEvent.Unblock(userId))
    }
}