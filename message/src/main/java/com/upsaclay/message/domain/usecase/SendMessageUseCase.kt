package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.ConversationRepository

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository
) {
    suspend operator fun invoke(conversationId: String, message: Message) {
        conversationRepository.createMessage(conversationId, message)
    }
}