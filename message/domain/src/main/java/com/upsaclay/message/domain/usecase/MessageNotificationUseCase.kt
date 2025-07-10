package com.upsaclay.message.domain.usecase

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.LocalDateTimeAdapter
import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.mapper.toFcm

import java.time.LocalDateTime

const val MESSAGE_CHANNEL_NOTIFICATION_ID = "message_channel_notification_id"

class MessageNotificationUseCase(
    private val notificationApi: NotificationApi,
    private val userRepository: UserRepository,
): NotificationUseCase<NotificationMessage>() {
    override suspend fun sendNotification(data: NotificationMessage) {
        userRepository.currentUser?.let {
            val fcmMessage = data.toFcm(it)
            val gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()

            notificationApi.sendNotification(data.conversation.interlocutor.id, fcmMessage, gson)
        }
    }
}