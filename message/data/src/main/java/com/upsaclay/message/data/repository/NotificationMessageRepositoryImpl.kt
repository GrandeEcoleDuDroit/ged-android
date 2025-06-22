package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.NotificationMessageLocalDataSource
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.repository.NotificationMessageRepository

class NotificationMessageRepositoryImpl(
    private val notificationMessageLocalDataSource: NotificationMessageLocalDataSource
): NotificationMessageRepository {
    override suspend fun getNotificationMessages(conversationId: String): List<NotificationMessage> =
        notificationMessageLocalDataSource.getNotificationMessages(conversationId)

    override suspend fun storeNotificationMessage(notificationMessage: NotificationMessage) {
        notificationMessageLocalDataSource.storeNotificationMessage(notificationMessage)
    }

    override suspend fun deleteNotificationMessages(conversationId: String) {
        notificationMessageLocalDataSource.deleteNotificationMessages(conversationId)
    }
}