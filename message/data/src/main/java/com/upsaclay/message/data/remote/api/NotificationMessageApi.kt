package com.upsaclay.message.data.remote.api

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.NotificationMessage

interface NotificationMessageApi {
    suspend fun sendNotification(currentUser: User, notificationMessage: NotificationMessage)
}