package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class RecreateConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, userId: String) {
        scope.launch {
            try {
                conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATING))
                conversationRepository.createRemoteConversation(conversation, userId)
                conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATED))
            } catch (e: Exception) {
                conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.ERROR))
            }
        }
    }
}