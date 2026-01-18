package com.upsaclay.message.domain.repository

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.MessageNotification

interface MessageNotificationRepository {
    fun parseNotification(value: String): MessageNotification

    suspend fun getMessageNotifications(conversationId: String): List<MessageNotification>

    suspend fun storeMessageNotification(messageNotification: MessageNotification)

    suspend fun deleteMessageNotifications(conversationId: String)

    suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification)
}