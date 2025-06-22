package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.NotificationMessageDao
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.data.mapper.toNotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationMessageLocalDataSource(
    private val notificationMessageDao: NotificationMessageDao
) {
    suspend fun getNotificationMessages(conversationId: String): List<NotificationMessage> = withContext(Dispatchers.IO) {
        notificationMessageDao.getNotificationMessages(conversationId)
            .map { it.toNotificationMessage() }
    }

    suspend fun storeNotificationMessage(notificationMessage: NotificationMessage) {
        withContext(Dispatchers.IO) {
            notificationMessageDao.insertNotificationMessage(notificationMessage.toLocal())
        }
    }

    suspend fun deleteNotificationMessages(conversationId: String) {
        withContext(Dispatchers.IO) {
            notificationMessageDao.deleteNotificationMessages(conversationId)
        }
    }
}