package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository

class StartConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(conversation: Conversation, message: Message) {
        conversationRepository.createConversation(conversation)
        messageRepository.sendMessage(conversation.id, message)
    }
}