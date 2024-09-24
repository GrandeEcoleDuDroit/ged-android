package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: Int): User? = userRepository.getUser(userId)
}