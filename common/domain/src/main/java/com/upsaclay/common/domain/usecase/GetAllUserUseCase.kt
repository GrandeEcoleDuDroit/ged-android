package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository

class GetAllUserUseCase(private val userRepository: com.upsaclay.common.domain.repository.UserRepository) {
    suspend operator fun invoke(): List<com.upsaclay.common.domain.model.User> = userRepository.getAllUsers()
}