package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.model.ConversationPreview
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow

class GetAllConversationsPreviewUseCase(
    private val conversationRepository: ConversationRepository
) {
    operator fun invoke(): Flow<List<ConversationPreview>> = conversationRepository.conversationsPreview
}