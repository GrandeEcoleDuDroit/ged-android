package com.upsaclay.app.data.repository

import com.google.firebase.messaging.FirebaseMessaging
import com.upsaclay.app.data.local.FcmLocalDataSource
import com.upsaclay.app.domain.entity.FcmToken
import com.upsaclay.app.domain.repository.FcmTokenRepository
import com.upsaclay.common.data.d
import com.upsaclay.common.data.e
import com.upsaclay.common.data.formatHttpError
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.entity.InternalServerException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FcmTokenRepositoryImpl(
    private val fcmLocalDataSource: FcmLocalDataSource,
    private val fcmApi: FcmApi,
): FcmTokenRepository {
    override suspend fun generateToken(): String = FirebaseMessaging.getInstance().token.await()

    override suspend fun getUnsentFcmToken(): FcmToken? = fcmLocalDataSource.getUnsentFcmToken()

    override suspend fun sendFcmToken(token: FcmToken) {
        withContext(Dispatchers.IO) {
            val response = token.userId?.let {
                fcmApi.addToken(it, token.value)
            } ?: run {
                e("User ID is null, cannot send FCM token")
                throw IllegalArgumentException("User ID is null")
            }

            if (!response.isSuccessful) {
                val errorMessage = formatHttpError(response)
                throw InternalServerException(errorMessage)
            } else {
                d(response.body()?.message ?: "Token sent successfully")
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