package com.upsaclay.common.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository

class GetCurrentUserUseCase(private val userRepository: com.upsaclay.common.domain.repository.UserRepository) {
    operator fun invoke(): com.upsaclay.common.domain.model.User? = userRepository.currentUser
}