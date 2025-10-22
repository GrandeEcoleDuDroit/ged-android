package com.upsaclay.message.data.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.local.NotificationMessageLocalDataSource
import com.upsaclay.message.data.remote.NotificationMessageRemoteDataSource
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.repository.NotificationMessageRepository

class NotificationMessageRepositoryImpl(
    private val notificationMessageLocalDataSource: NotificationMessageLocalDataSource,
    private val notificationMessageRemoteDataSource: NotificationMessageRemoteDataSource
): NotificationMessageRepository {
    override suspend fun getNotificationMessages(conversationId: String): List<NotificationMessage> =
        notificationMessageLocalDataSource.getNotificationMessages(conversationId)

    override suspend fun storeNotificationMessage(notificationMessage: NotificationMessage) {
        notificationMessageLocalDataSource.storeNotificationMessage(notificationMessage)
    }

    override suspend fun deleteNotificationMessages(conversationId: String) {
        notificationMessageLocalDataSource.deleteNotificationMessages(conversationId)
    }

    override suspend fun sendNotification(currentUser: User, notificationMessage: NotificationMessage) {
        notificationMessageRemoteDataSource.sendNotification(currentUser, notificationMessage)
    }
}