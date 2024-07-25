package com.upsaclay.authentication.domain

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.authentication.data.model.AuthenticationState

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthenticationState> {
        return authenticationRepository.login(email, password)
    }
}