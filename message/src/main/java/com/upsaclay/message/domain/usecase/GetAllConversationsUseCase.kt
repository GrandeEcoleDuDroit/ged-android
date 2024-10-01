package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.repository.UserConversationRepository
import kotlinx.coroutines.flow.Flow

class GetAllConversationsUseCase(
    private val userConversationRepository: UserConversationRepository
) {
    operator fun invoke(): Flow<List<Conversation>> = userConversationRepository.conversations
}