package com.upsaclay.message.data.remote.api

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.MessageNotification

interface MessageNotificationApi {
    suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification)
}