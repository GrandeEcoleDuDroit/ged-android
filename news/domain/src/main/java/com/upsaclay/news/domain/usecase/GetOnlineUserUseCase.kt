package com.upsaclay.news.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository

class GetOnlineUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.getOnlineUsers()
}