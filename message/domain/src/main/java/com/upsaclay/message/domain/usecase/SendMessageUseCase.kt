package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.usecase.GenerateIDUseCase
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.MessageRepository

class SendMessageUseCase(
    private val messageRepository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String, message: Message) {
        messageRepository.sendMessage(conversationId, message.copy(id = GenerateIDUseCase()))
    }
}