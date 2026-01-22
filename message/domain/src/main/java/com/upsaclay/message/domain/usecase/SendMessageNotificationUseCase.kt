package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.repository.MessageNotificationRepository

class SendMessageNotificationUseCase(
    private val userRepository: UserRepository,
    private val messageNotificationRepository: MessageNotificationRepository
) {
    suspend fun execute(conversation: Conversation, message: Message) {
        runCatching {
            val currentUser = userRepository.currentUser ?: return
            val messageContentNotification = MessageNotification(
                conversation = conversation,
                messageContent = MessageNotification.MessageContent(
                    messageId = message.id,
                    content = message.content,
                    timestamp = message.date.toEpochMilliUTC()
                )
            )
            messageNotificationRepository.sendNotification(currentUser, messageContentNotification)
        }
    }
}