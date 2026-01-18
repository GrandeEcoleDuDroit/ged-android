package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.remote.api.NotificationApi
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.mapper.toFcm
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.domain.entity.MessageNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageNotificationRemoteDataSource(private val notificationApi: NotificationApi) {
    suspend fun sendNotification(currentUser: User, notification: MessageNotification) {
        withContext(Dispatchers.IO) {
            try {
                val fcmMessage = notification.toRemote(currentUser).toFcm()
                notificationApi.sendNotification(currentUser.id, notification.conversation.interlocutor.id, fcmMessage)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}