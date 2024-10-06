package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.MessageRepository

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String?, message: Message) {
        conversationId?.let {
            messageRepository.sendMessage(conversationId, message)
        }
    }
}