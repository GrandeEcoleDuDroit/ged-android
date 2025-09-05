package com.upsaclay.common.data.remote

import com.google.gson.Gson
import com.upsaclay.common.data.remote.api.FcmApi
import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.fcm.FcmMessage

internal class NotificationApiImpl(private val fcmApi: FcmApi): NotificationApi {
    private val gson = Gson()
    
    override suspend fun <T> sendNotification(recipientId: String, fcmMessage: FcmMessage<T>) {
        try {
            val response = fcmApi.sendNotification(recipientId, gson.toJson(fcmMessage))
            if (!response.isSuccessful) {
                val errorMessage = response.errorBody()?.string()
                    ?: "Failed to send fcm notification: ${response.errorBody()}"
                e(errorMessage)
            }
        } catch (e: Exception) {
            val errorMessage = "Failed to send fcm notification: ${e.message}"
            e(errorMessage)
        }
    }
}