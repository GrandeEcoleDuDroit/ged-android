package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import java.time.LocalDateTime

class UpdateConversationDeleteTimeUseCase(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    suspend fun execute(userId: String, deleteTime: LocalDateTime) {
        val currentUserId = userRepository.currentUser?.id ?: return
        val conversation = conversationRepository.getConversation(userId)?.apply {
            copy(effectiveFrom = deleteTime)
        } ?: return
        conversationRepository.updateConversationEffectiveFrom(conversation, currentUserId, deleteTime)
    }
}