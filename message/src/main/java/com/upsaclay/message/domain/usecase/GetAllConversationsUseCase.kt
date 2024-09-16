package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.repository.ConversationRepository

class GetAllConversationsUseCase(
    private val conversationRepository: ConversationRepository
) {
    operator fun invoke() = conversationRepository.conversations
}