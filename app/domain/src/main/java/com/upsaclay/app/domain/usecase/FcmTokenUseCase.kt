package com.upsaclay.app.domain.usecase

import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.FcmTokenRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class FcmTokenUseCase(
    private val userRepository: UserRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val scope: CoroutineScope
) {
    suspend fun sendUnsentToken() {
        val currentUserId = userRepository.getCurrentUser()?.id ?: return
        fcmTokenRepository.getFcmToken()?.let { fcmToken ->
            if (!fcmToken.sent) {
                addFcmToken(currentUserId, fcmToken)
            }
        } ?: run {
            val token = fcmTokenRepository.generateToken()
            addFcmToken(currentUserId, FcmToken(token, false))
        }
    }
    
    fun onNewTokenReceived(token: String) {
        scope.launch {
            if (token == fcmTokenRepository.getFcmToken()?.token) return@launch

            val fcmToken = FcmToken(token, false)
            fcmTokenRepository.storeFcmToken(fcmToken)
            userRepository.getCurrentUser()?.id?.let {
                addFcmToken(it, fcmToken)
            }
        }
    }

    private suspend fun addFcmToken(userId: String, fcmToken: FcmToken) {
        try {
            fcmTokenRepository.sendFcmToken(userId, fcmToken.token!!)
            fcmTokenRepository.storeFcmToken(fcmToken.copy(sent = true))
        } catch (e: Exception) {
            fcmTokenRepository.storeFcmToken(fcmToken.copy(sent = false))
        }
    }
}

