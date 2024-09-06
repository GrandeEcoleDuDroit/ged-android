package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserUseCase(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<User> = userRepository.user
}