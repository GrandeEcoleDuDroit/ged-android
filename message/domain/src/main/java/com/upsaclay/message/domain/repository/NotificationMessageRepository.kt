package com.upsaclay.message.domain.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.NotificationMessage

interface NotificationMessageRepository {
    suspend fun getNotificationMessages(conversationId: String): List<NotificationMessage>

    suspend fun storeNotificationMessage(notificationMessage: NotificationMessage)

    suspend fun deleteNotificationMessages(conversationId: String)

    suspend fun sendNotification(currentUser: User, notificationMessage: NotificationMessage)
}