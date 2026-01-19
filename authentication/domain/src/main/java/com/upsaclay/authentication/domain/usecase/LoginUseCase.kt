package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.entity.AuthenticationException
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.AUTH_USER_NOT_FOUND
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.withTimeout

class LoginUseCase(private val authenticationRepository: AuthenticationRepository) {
    suspend operator fun invoke(email: String, password: String) {
        val userId = withTimeout(10000) {
            authenticationRepository.loginWithEmailAndPassword(email, password)
        } ?: throw AuthenticationException(AUTH_USER_NOT_FOUND)
        authenticationRepository.storeAuthenticationState(AuthenticationState.Authenticated(userId))
    }
}