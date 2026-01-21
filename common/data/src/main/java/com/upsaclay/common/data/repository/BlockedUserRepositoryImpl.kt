package com.upsaclay.common.data.repository

import com.upsaclay.common.data.local.BlockedUserLocalDataSource
import com.upsaclay.common.data.remote.BlockedUserRemoteDataSource
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.entity.BlockedUser
import com.upsaclay.common.domain.entity.BlockedUsers
import com.upsaclay.common.domain.repository.BlockedUserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class BlockedUserRepositoryImpl(
    private val blockedUserLocalDataSource: BlockedUserLocalDataSource,
    private val blockedUserRemoteDataSource: BlockedUserRemoteDataSource,
    private val scope: CoroutineScope
): BlockedUserRepository {
    private val _blockUserEvent = MutableSharedFlow<BlockUserEvent>(replay = 1)
    override val blockUserEvent: Flow<BlockUserEvent> = _blockUserEvent

    private val _blockedUsers = MutableStateFlow<BlockedUsers?>(null)
    override val blockedUsers: Flow<BlockedUsers> = _blockedUsers.filterNotNull()
    override val currentBlockedUsers: BlockedUsers?
        get() = _blockedUsers.value

    init {
        listenBlockedUsers()
    }

    override suspend fun getLocalBlockedUsers(): BlockedUsers = blockedUserLocalDataSource.getBlockedUsers()

    override suspend fun getRemoteBlockedUsers(currentUserId: String): BlockedUsers {
        return try {
            blockedUserRemoteDataSource.getBlockedUsers(currentUserId).associateBy { it.userId }
        } catch (e: Exception) {
            e("Error getting remote blocked users for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun addBlockUser(currentUserId: String, blockedUser: BlockedUser) {
        try {
            blockedUserRemoteDataSource.addBlockedUser(currentUserId, blockedUser)
            blockedUserLocalDataSource.addBlockUser(blockedUser)
            _blockUserEvent.emit(BlockUserEvent.Block(blockedUser))
        } catch (e: Exception) {
            e("Error blocking user ${blockedUser.userId} for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun addLocalBlockUser(blockedUser: BlockedUser) {
        blockedUserLocalDataSource.addBlockUser(blockedUser)
    }

    override suspend fun removeBlockedUser(currentUserId: String, blockedUserId: String) {
        try {
            blockedUserRemoteDataSource.removeBlockedUser(currentUserId, blockedUserId)
            blockedUserLocalDataSource.removeBlockUser(blockedUserId)
            _blockUserEvent.emit(BlockUserEvent.Unblock(blockedUserId))
        } catch (e: Exception) {
            e("Error unblocking user $blockedUserId for user $currentUserId", e)
            throw e
        }
    }

    override suspend fun removeLocalBlockedUser(blockedUserId: String) {
        blockedUserLocalDataSource.removeBlockUser(blockedUserId)
    }
    override suspend fun deleteLocalBlockedUsers() {
        blockedUserLocalDataSource.deleteBlockedUsers()
    }

    private fun listenBlockedUsers() {
        scope.launch {
            blockedUserLocalDataSource.getBlockedUsersFlow().collect { blockedUsers ->
                _blockedUsers.update { blockedUsers }
            }
        }
    }
}