package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationDTO
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ConversationRepository {
    suspend fun getConversations(): List<Conversation>

    fun getConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getConversation(interlocutorId: String): Conversation?

    suspend fun getRemoteConversationsFlow(userId: String): Flow<ConversationDTO>

    suspend fun createLocalConversation(conversation: Conversation)

    suspend fun createRemoteConversation(conversation: Conversation, userId: String)

    suspend fun updateConversationEffectiveFrom(conversation: Conversation, currentUserId: String, effectiveFrom: LocalDateTime)

    suspend fun updateLocalConversation(conversation: Conversation)

    suspend fun upsertLocalConversation(conversation: Conversation)

    suspend fun deleteConversation(conversationId: String, currentUserId: String, deleteTime: LocalDateTime)

    suspend fun deleteLocalConversations()
}
