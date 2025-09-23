package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository

class SynchronizeBlockedUserUseCase(
    private val blockedUserRepository: BlockedUserRepository,
    private val userRepository: UserRepository
) {
    suspend fun synchronize() {
        val currentUserId = userRepository.currentUser?.id ?: return
        val remoteBlockedUserIds = blockedUserRepository.getRemoteBlockedUserIds(currentUserId)
        val localBlockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

        val toBlock = remoteBlockedUserIds - localBlockedUserIds
        val toUnblock = localBlockedUserIds - remoteBlockedUserIds

        toBlock.forEach { blockedUserId ->
            blockedUserRepository.blockLocalUser(blockedUserId)
        }

        toUnblock.forEach { blockedUserId ->
            blockedUserRepository.unblockLocalUser(blockedUserId)
        }
    }
}