package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllConversationsUseCase(private val conversationRepository: ConversationRepository) {
    operator fun invoke(): Flow<List<Conversation>> = conversationRepository.conversations.map { conversations ->
        if (conversations.isNotEmpty()) {
            conversations.sortedByDescending { it.messages.firstOrNull()?.date }
        } else {
            emptyList()
        }
    }
}