package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository

class FetchCurrentUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String) {
        userRepository.getUser(userId)?.let {
            userRepository.storeUser(it)
        }
    }
}