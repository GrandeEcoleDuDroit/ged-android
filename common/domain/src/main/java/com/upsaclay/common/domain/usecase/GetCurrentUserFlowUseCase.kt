package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetCurrentUserFlowUseCase(private val userRepository: com.upsaclay.common.domain.repository.UserRepository) {
    operator fun invoke(): Flow<com.upsaclay.common.domain.model.User> = userRepository.currentUserFlow
}