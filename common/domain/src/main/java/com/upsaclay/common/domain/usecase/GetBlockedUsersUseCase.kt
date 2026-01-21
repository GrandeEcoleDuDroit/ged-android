package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class GetBlockedUsersUseCase(
    private val blockedUserRepository: BlockedUserRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): List<User> = coroutineScope {
        val blockedUsers = blockedUserRepository.getLocalBlockedUsers().values
        blockedUsers.map {
            async { userRepository.getUser(it.userId) }
        }
        .awaitAll()
        .filterNotNull()
    }
}