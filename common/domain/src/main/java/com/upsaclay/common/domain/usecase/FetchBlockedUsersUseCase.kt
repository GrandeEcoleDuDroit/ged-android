package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository

class FetchBlockedUsersUseCase(private val blockedUserRepository: BlockedUserRepository) {
    suspend operator fun invoke(userId: String) {
        val remoteBlockedUsers = blockedUserRepository.getRemoteBlockedUsers(userId)
        val localBlockedUsers = blockedUserRepository.getLocalBlockedUsers()

        val usersToBlock = (remoteBlockedUsers - localBlockedUsers).values
        val usersToUnblock = (localBlockedUsers - remoteBlockedUsers).values

        usersToBlock.forEach { blockedUser ->
            blockedUserRepository.addLocalBlockUser(blockedUser)
        }

        usersToUnblock.forEach { blockedUser ->
            blockedUserRepository.removeLocalBlockedUser(blockedUser.userId)
        }
    }
}