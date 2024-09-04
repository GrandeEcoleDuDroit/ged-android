package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val generateHashUseCase: GenerateHashUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        val hash = generateHashUseCase()
        return authenticationRepository.loginWithParisSaclay(email, password, hash)
    }
}