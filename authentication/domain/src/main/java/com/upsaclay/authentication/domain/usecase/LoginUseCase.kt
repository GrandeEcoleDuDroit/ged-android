package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.entity.AuthenticationException
import com.upsaclay.authentication.domain.entity.AuthenticationException.AuthenticationError.AUTH_USER_NOT_FOUND
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.withTimeout

class LoginUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String) {
        val user = withTimeout(10000) {
            authenticationRepository.loginWithEmailAndPassword(email, password)
        }?.let { uid ->
            userRepository.getUser(uid)
        }

        user?.let {
            userRepository.storeUser(it)
            authenticationRepository.setAuthenticated(true)
        } ?: throw AuthenticationException(AUTH_USER_NOT_FOUND)
    }
}