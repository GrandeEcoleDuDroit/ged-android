package com.upsaclay.app.domain.usecase

import com.upsaclay.app.domain.entity.FcmToken
import com.upsaclay.app.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository

class FcmTokenUseCase(
    private val userRepository: UserRepository,
    private val fcmTokenRepository: FcmTokenRepository
) {
    suspend fun sendUnsetToken() {
        fcmTokenRepository.getUnsentFcmToken()?.let { fcmToken ->
            val userId = fcmToken.userId ?: userRepository.currentUser?.id ?: return@let
            sendFcmToken(fcmToken.copy(userId = userId))
        }
    }

    suspend fun generateNewToken() {
        fcmTokenRepository.removeUnsentFcmToken()
        val token = fcmTokenRepository.generateToken()
        fcmTokenRepository.storeUnsentFcmToken(FcmToken(null, token))
    }

    suspend fun sendFcmToken(fcmToken: FcmToken) {
        runCatching {
            fcmTokenRepository.sendFcmToken(fcmToken)
            fcmTokenRepository.removeUnsentFcmToken()
        }
            .onFailure {
                fcmTokenRepository.storeUnsentFcmToken(fcmToken)
            }
    }

    suspend fun storeToken(fcmToken: FcmToken) {
        fcmTokenRepository.storeUnsentFcmToken(fcmToken)
    }
}