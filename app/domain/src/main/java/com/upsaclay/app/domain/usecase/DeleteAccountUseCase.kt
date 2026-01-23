package com.upsaclay.app.domain.usecase

import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository

class DeleteAccountUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend fun execute(user: User, password: String) {
        authenticationRepository.loginWithEmailAndPassword(user.email, password)
        userRepository.deleteUser(user)
        authenticationRepository.storeAuthenticationState(AuthenticationState.Unauthenticated)
    }
}