package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.remote.api.MessageNotificationApi
import com.upsaclay.message.domain.entity.MessageNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageNotificationRemoteDataSource(private val messageNotificationApi: MessageNotificationApi) {
    suspend fun sendNotification(currentUser: User, messageNotification: MessageNotification) {
        withContext(Dispatchers.IO) {
            try {
                messageNotificationApi.sendNotification(currentUser, messageNotification)
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}