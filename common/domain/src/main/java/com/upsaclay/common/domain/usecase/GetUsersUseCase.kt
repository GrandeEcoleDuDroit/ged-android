package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository

class GetUsersUseCase(
    private val userRepository: UserRepository
) {
    suspend fun execute() = userRepository.getUsers().filter {
        it.state != User.UserState.DELETED
    }
}