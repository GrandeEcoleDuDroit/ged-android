package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository

class FetchBlockedUsersUseCase(private val blockedUserRepository: BlockedUserRepository) {
    suspend operator fun invoke(userId: String) {
        val remoteBlockedUserIds = blockedUserRepository.getRemoteBlockedUserIds(userId)
        val localBlockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

        val usersToBlock = remoteBlockedUserIds - localBlockedUserIds
        val usersToUnblock = localBlockedUserIds - remoteBlockedUserIds

        usersToBlock.forEach { id ->
            blockedUserRepository.blockLocalUser(id)
        }

        usersToUnblock.forEach { id ->
            blockedUserRepository.unblockLocalUser(id)
        }
    }
}