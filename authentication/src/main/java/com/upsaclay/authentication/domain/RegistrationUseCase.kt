package com.upsaclay.authentication.domain

import com.upsaclay.authentication.data.AuthenticationRepository
import com.upsaclay.core.data.model.User

class RegistrationUseCase(
    private val authenticationRepository: AuthenticationRepository
) {
    suspend operator fun invoke(user: User): Result<Int> =
        authenticationRepository.createUser(user)
}