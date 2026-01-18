package com.upsaclay.app.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.upsaclay.app.data.local.FcmLocalDataSource
import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.data.utils.sendServerRequest
import com.upsaclay.common.domain.entity.FcmToken
import com.upsaclay.common.domain.repository.FcmTokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FcmTokenRepositoryImpl(
    private val fcmLocalDataSource: FcmLocalDataSource,
    private val fcmApi: FcmApi
): FcmTokenRepository {
    override suspend fun generateToken(): String = FirebaseMessaging.getInstance().token.await()

    override suspend fun getFcmToken(): FcmToken? = fcmLocalDataSource.getFcmToken()

    override suspend fun sendFcmToken(userId: String, token: String) {
        withContext(Dispatchers.IO) {
            try {
                sendServerRequest { fcmApi.addToken(userId, token) }
            } catch (e: Exception) {
                e("Error sending FCM token for user $userId", e)
                throw mapServerException(e)
            }
        }
    }

    override suspend fun storeFcmToken(fcmToken: FcmToken) {
        fcmLocalDataSource.storeFcmToken(fcmToken)
    }

    override suspend fun deleteToken(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                val token = getFcmToken()?.token ?: return@withContext
                sendServerRequest { fcmApi.deleteToken(userId, token) }
                fcmLocalDataSource.deleteFcmToken()
            } catch (e: Exception) {
                e("Error deleting FCM token for user $userId", e)
                throw mapServerException(e)
            }
        }
    }
}