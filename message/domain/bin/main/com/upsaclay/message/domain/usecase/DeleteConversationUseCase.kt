package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime
import java.time.ZoneOffset

class DeleteConversationUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
) {
    suspend fun execute(conversation: Conversation, currentUserId: String) {
        val deleteTime = LocalDateTime.now(ZoneOffset.UTC)
        withTimeout(10000) {
            conversationRepository.deleteConversation(conversation.id, currentUserId, deleteTime)
        }
        messageRepository.deleteLocalMessages(conversation.id)
    }
}