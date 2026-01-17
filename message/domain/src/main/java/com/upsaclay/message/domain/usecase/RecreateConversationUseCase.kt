package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository

class RecreateConversationUseCase(private val conversationRepository: ConversationRepository) {
    suspend operator fun invoke(conversation: Conversation, userId: String) {
        try {
            conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATING))
            conversationRepository.createRemoteConversation(conversation, userId)
            conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATED))
        } catch (e: Exception) {
            conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.ERROR))
        }
    }
}