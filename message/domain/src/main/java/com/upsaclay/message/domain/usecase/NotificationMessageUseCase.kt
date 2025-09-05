package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.NotificationApi
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.NotificationUseCase
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.mapper.toFcm
import com.upsaclay.message.domain.mapper.toRemote

class NotificationMessageUseCase(
    private val notificationApi: NotificationApi,
    private val userRepository: UserRepository,
): NotificationUseCase<NotificationMessage>() {
    override suspend fun sendNotification(data: NotificationMessage) {
        userRepository.currentUser?.let {
            val fcmMessage = data.toRemote(it).toFcm()
            notificationApi.sendNotification(data.conversation.interlocutor.id, fcmMessage)
        }
    }
}