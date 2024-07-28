package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.model.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import okio.IOException

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val generateHashUseCase: GenerateHashUseCase
) {
    suspend operator fun invoke(email: String, password: String): Result<AuthenticationState> {
        val hash = generateHashUseCase()
        val result = authenticationRepository.loginWithParisSaclay(email, password, hash)
        return if(result.isSuccess) {
            Result.success(AuthenticationState.AUTHENTICATED)
        }
        else {
            Result.failure(result.exceptionOrNull() ?: IOException("Error during login"))
        }
    }
}