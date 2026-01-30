package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository

class FetchCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: String) {
        userRepository.getUser(userId)?.let {
            userRepository.storeUser(it)
        }
    }
}