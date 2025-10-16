package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface ConversationRepository {
    suspend fun getConversations(): List<Conversation>

    fun getConversationFlow(interlocutorId: String): Flow<Conversation>

    suspend fun getConversation(interlocutorId: String): Conversation?

    suspend fun fetchRemoteConversation(userId: String): Flow<Conversation>

    suspend fun createLocalConversation(conversation: Conversation)

    suspend fun createRemoteConversation(conversation: Conversation, userId: String)

    suspend fun updateConversationDeleteTime(conversation: Conversation, currentUserId: String, deleteTime: LocalDateTime)

    suspend fun updateLocalConversation(conversation: Conversation)

    suspend fun upsertLocalConversation(conversation: Conversation)

    suspend fun deleteConversation(conversationId: String, currentUserId: String, deleteTime: LocalDateTime)

    suspend fun deleteLocalConversations()
}
