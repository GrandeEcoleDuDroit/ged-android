package com.upsaclay.message.data.repository

import com.google.gson.Gson
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.local.MessageNotificationLocalDataSource
import com.upsaclay.message.data.mapper.toMessageNotification
import com.upsaclay.message.data.remote.MessageNotificationRemoteDataSource
import com.upsaclay.message.data.remote.model.RemoteMessageNotification
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.repository.MessageNotificationRepository

class MessageNotificationRepositoryImpl(
    private val messageNotificationLocalDataSource: MessageNotificationLocalDataSource,
    private val messageNotificationRemoteDataSource: MessageNotificationRemoteDataSource
): MessageNotificationRepository {
    private val gson = Gson()

    override fun parseNotification(value: String): MessageNotification =
        gson.fromJson(value, RemoteMessageNotification::class.java).toMessageNotification()

    override suspend fun getMessageNotifications(conversationId: String): List<MessageNotification> =
        messageNotificationLocalDataSource.getMessageNotifications(conversationId)

    override suspend fun storeMessageNotification(messageNotification: MessageNotification) {
        messageNotificationLocalDataSource.storeMessageNotification(messageNotification)
    }

    override suspend fun deleteMessageNotifications(conversationId: String) {
        messageNotificationLocalDataSource.deleteMessageNotifications(conversationId)
    }

    override suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification) {
        try {
            messageNotificationRemoteDataSource.sendNotification(currentUser, messageNotification)
        } catch (e: Exception) {
            e("Error sending message notification for conversation ${messageNotification.conversation.id}", e)
            throw e
        }
    }
}