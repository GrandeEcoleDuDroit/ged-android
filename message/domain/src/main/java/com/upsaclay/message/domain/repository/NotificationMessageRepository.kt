package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.NotificationMessage

interface NotificationMessageRepository {
    suspend fun getNotificationMessages(conversationId: String): List<NotificationMessage>

    suspend fun storeNotificationMessage(notificationMessage: NotificationMessage)

    suspend fun deleteNotificationMessages(conversationId: String)
}