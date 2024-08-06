package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.infoLog

class RegistrationUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
) {
    suspend operator fun invoke(user: User): Result<Int> {
        val createUserResult = userRepository.createUser(user)

        return if(createUserResult.isSuccess) {
            infoLog("Registration successful")
            authenticationRepository.setAuthenticated()
            createUserResult
        }
        else {
            errorLog("Error registration" ,createUserResult.exceptionOrNull())
            Result.failure(createUserResult.exceptionOrNull()!!)
        }
    }
}