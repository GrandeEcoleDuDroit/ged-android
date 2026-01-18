package com.upsaclay.common.data.remote.api

import com.upsaclay.common.domain.entity.fcm.FcmMessage

interface NotificationApi {
    suspend fun <T>sendNotification(userId: String, recipientId: String, fcmMessage: FcmMessage<T>)
}