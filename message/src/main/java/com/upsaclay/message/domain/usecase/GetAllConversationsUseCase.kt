package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.repository.UserConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAllConversationsUseCase(
    private val userConversationRepository: UserConversationRepository
) {
    operator fun invoke(): Flow<List<Conversation>> {
        return userConversationRepository.conversations.map { conversations ->
            if (conversations.isNotEmpty()) {
                conversations.sortedByDescending { it.messages.firstOrNull()?.date }
            } else emptyList()
        }
    }
}