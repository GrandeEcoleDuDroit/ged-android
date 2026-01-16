package com.upsaclay.app.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.upsaclay.app.data.local.FcmLocalDataSource
import com.upsaclay.app.domain.entity.FcmToken
import com.upsaclay.app.domain.repository.FcmTokenRepository
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.common.data.remote.api.FcmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FcmTokenRepositoryImpl(
    private val fcmLocalDataSource: FcmLocalDataSource,
    private val fcmApi: FcmApi
): FcmTokenRepository {
    override suspend fun generateToken(): String = FirebaseMessaging.getInstance().token.await()

    override suspend fun getUnsentFcmToken(): FcmToken? = fcmLocalDataSource.getUnsentFcmToken()

    override suspend fun sendFcmToken(token: FcmToken) {
        withContext(Dispatchers.IO) {
            try {
                token.userId?.let {
                    sendServerRequest { fcmApi.addToken(it, token.value) }
                }
            } catch (e: Exception) {
                e("Error sending FCM token for user ${token.userId}", e)
                throw mapServerException(e)
            }
        }
    }

    override suspend fun storeUnsentFcmToken(token: FcmToken) {
        fcmLocalDataSource.storeUnsentFcmToken(token)
    }

    override suspend fun removeUnsentFcmToken() {
        fcmLocalDataSource.removeUnsentFcmToken()
    }

    override fun deleteToken() {
        FirebaseMessaging.getInstance().deleteToken()
    }
}