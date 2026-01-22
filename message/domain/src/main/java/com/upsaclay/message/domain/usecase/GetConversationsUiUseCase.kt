package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.mapper.toConversationUI
import com.upsaclay.message.domain.repository.ConversationMessageRepository
import kotlinx.coroutines.flow.map

class GetConversationsUiUseCase(
    private val conversationMessageRepository: ConversationMessageRepository
) {
    fun execute() = conversationMessageRepository.conversationsMessage
        .map { conversationMessages ->
            conversationMessages.map { it.toConversationUI() }
        }
}