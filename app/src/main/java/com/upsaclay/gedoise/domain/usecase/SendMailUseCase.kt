package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository

class SendMailUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(){
        val user : User? = userRepository.getCurrentUser()
        val mail : String = getUserMail(user)
    }

    private fun getUserMail(user: User?): String {
        return user?.email ?: ""
    }

}
