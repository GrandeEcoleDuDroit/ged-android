package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository

class FetchBlockedUsersUseCase(
    private val blockedUserRepository: BlockedUserRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() {
        val currentUserId = userRepository.currentUser?.id ?: return
        val remoteBlockedUserIds = blockedUserRepository.getRemoteBlockedUserIds(currentUserId)
        val localBlockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

        val usersToBlock = remoteBlockedUserIds - localBlockedUserIds
        val usersToUnblock = localBlockedUserIds - remoteBlockedUserIds

        usersToBlock.forEach { userId ->
            blockedUserRepository.blockLocalUser(userId)
        }

        usersToUnblock.forEach { userId ->
            blockedUserRepository.unblockLocalUser(userId)
        }
    }
}