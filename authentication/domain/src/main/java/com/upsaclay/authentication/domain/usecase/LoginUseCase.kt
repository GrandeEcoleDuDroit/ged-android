package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        withTimeout(10000) {
            authenticationRepository.loginWithEmailAndPassword(email, password)?.let { userId  ->
                userRepository.getUser(userId)?.let {
                    userRepository.storeUser(it)
                    authenticationRepository.setAuthenticated(true)
                } ?: throw InvalidCredentialsException()
            } ?: throw IllegalArgumentException()
        }
    }
}