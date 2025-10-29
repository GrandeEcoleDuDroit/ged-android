package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.MessageNotificationDao
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.data.mapper.toMessageNotification
import com.upsaclay.message.domain.entity.MessageNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageNotificationLocalDataSource(
    private val messageNotificationDao: MessageNotificationDao
) {
    suspend fun getMessageNotifications(conversationId: String): List<MessageNotification> = withContext(Dispatchers.IO) {
        messageNotificationDao.getMessageNotifications(conversationId)
            .map { it.toMessageNotification() }
    }

    suspend fun storeMessageNotification(messageNotification: MessageNotification) {
        withContext(Dispatchers.IO) {
            messageNotificationDao.insertMessageNotification(messageNotification.toLocal())
        }
    }

    suspend fun deleteMessageNotifications(conversationId: String) {
        withContext(Dispatchers.IO) {
            messageNotificationDao.deleteMessageNotifications(conversationId)
        }
    }
}