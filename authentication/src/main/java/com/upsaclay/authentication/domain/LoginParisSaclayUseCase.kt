package com.upsaclay.authentication.domain

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.AuthenticationState

class LoginParisSaclayUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val generateHashUseCase: GenerateHashUseCase,
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthenticationState> {
        val hash = generateHashUseCase()
        return authenticationRepository.loginWithParisSaclay(email, password, hash)
    }
}