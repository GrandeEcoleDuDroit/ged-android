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

    override suspend fun getBlockedUserIds(): Set<String> = blockedUserLocalDataSource.getBlockedUserIds()

    override suspend fun blockUser(currentUserId: String, blockedUserId: String) {
        blockedUserRemoteDataSource.blockUser(currentUserId, blockedUserId)
        blockedUserLocalDataSource.blockUser(blockedUserId)
        _blockUserEvent.emit(BlockUserEvent.Block(blockedUserId))
    }

    override suspend fun unblockUser(currentUserId: String, blockedUserId: String) {
        blockedUserRemoteDataSource.unblockUser(currentUserId, blockedUserId)
        blockedUserLocalDataSource.unblockUser(blockedUserId)
        _blockUserEvent.emit(BlockUserEvent.Unblock(blockedUserId))
    }
}