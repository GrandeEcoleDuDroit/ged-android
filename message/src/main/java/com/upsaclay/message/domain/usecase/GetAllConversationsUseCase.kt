package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.data.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow

class GetAllConversationsUseCase(
    private val conversationRepository: ConversationRepository
) {
    operator fun invoke(): Flow<List<Conversation>> = conversationRepository.conversations

}