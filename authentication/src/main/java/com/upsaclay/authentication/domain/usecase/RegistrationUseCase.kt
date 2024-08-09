package com.upsaclay.authentication.domain.usecase

import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.errorLog
import com.upsaclay.common.utils.infoLog

class RegistrationUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(user: User): Result<Int> {
        val result = userRepository.createUser(user)

        return if(result.isSuccess) {
            infoLog("Registration successful")
            result
        }
        else {
            errorLog("Error registration" ,result.exceptionOrNull())
            Result.failure(result.exceptionOrNull()!!)
        }
    }
}