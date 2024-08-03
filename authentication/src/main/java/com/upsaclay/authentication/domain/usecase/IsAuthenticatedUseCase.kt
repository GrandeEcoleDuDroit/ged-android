package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository

class IsAuthenticatedUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(): Boolean = authenticationRepository.isAuthenticated()
}