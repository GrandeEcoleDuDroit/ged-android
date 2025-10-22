package com.upsaclay.message.data.remote

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.data.remote.api.NotificationMessageApi
import com.upsaclay.message.domain.entity.NotificationMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationMessageRemoteDataSource(private val notificationMessageApi: NotificationMessageApi) {
    suspend fun sendNotification(currentUser: User, notificationMessage: NotificationMessage) {
        withContext(Dispatchers.IO) {
            notificationMessageApi.sendNotification(currentUser, notificationMessage)
        }
    }
}