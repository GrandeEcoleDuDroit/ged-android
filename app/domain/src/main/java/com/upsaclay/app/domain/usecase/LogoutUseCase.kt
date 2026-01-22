package com.upsaclay.app.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository

class LogoutUseCase(
    private val userRepository: UserRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val fcmTokenRepository: FcmTokenRepository
) {
    suspend fun execute() {
        val userId = userRepository.currentUser?.id ?: throw CustomException(CustomException.CustomError.CURRENT_USER_NOT_FOUND)
        fcmTokenRepository.deleteToken(userId)
        authenticationRepository.logout()
    }
}