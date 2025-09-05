package com.upsaclay.common.domain

import com.upsaclay.common.domain.entity.fcm.FcmMessage

interface NotificationApi {
    suspend fun <T> sendNotification(recipientId: String, fcmMessage: FcmMessage<T>)
}