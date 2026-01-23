package com.upsaclay.common.data.remote.api

import com.google.gson.Gson
import com.upsaclay.common.domain.entity.fcm.FcmMessage

class NotificationApiImpl(private val fcmApi: FcmApi): NotificationApi {
    private val gson = Gson()

    override suspend fun <T> sendNotification(userId: String, recipientId: String, fcmMessage: FcmMessage<T>) {
        val fcmJson = gson.toJson(fcmMessage)
        fcmApi.sendNotification(userId, recipientId, fcmJson)
    }
}