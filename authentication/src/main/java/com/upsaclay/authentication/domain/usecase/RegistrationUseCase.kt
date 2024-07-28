package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.core.domain.model.User

class RegistrationUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(user: User): Result<Int> = authenticationRepository.register(user)
}