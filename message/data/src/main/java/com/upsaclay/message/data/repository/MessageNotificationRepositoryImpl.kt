package com.upsaclay.message.data.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.local.MessageNotificationLocalDataSource
import com.upsaclay.message.data.remote.MessageNotificationRemoteDataSource
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.repository.MessageNotificationRepository

class MessageNotificationRepositoryImpl(
    private val messageNotificationLocalDataSource: MessageNotificationLocalDataSource,
    private val messageNotificationRemoteDataSource: MessageNotificationRemoteDataSource
): MessageNotificationRepository {
    override suspend fun getMessageNotifications(conversationId: String): List<MessageNotification> =
        messageNotificationLocalDataSource.getMessageNotifications(conversationId)

    override suspend fun storeMessageNotification(messageNotification: MessageNotification) {
        messageNotificationLocalDataSource.storeMessageNotification(messageNotification)
    }

    override suspend fun deleteMessageNotifications(conversationId: String) {
        messageNotificationLocalDataSource.deleteMessageNotifications(conversationId)
    }

    override suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification) {
        messageNotificationRemoteDataSource.sendNotification(currentUser, messageNotification)
    }
}