package com.upsaclay.message.data.repository

import com.upsaclay.common.data.utils.e
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.mapper.toDTO
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationDTO
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

internal class ConversationRepositoryImpl(
    private val conversationLocalDataSource: ConversationLocalDataSource,
    private val conversationRemoteDataSource: ConversationRemoteDataSource,
) : ConversationRepository {
    override fun getConversationsFlow(): Flow<List<Conversation>> =
        conversationLocalDataSource.getConversationsFlow()

    override suspend fun getConversations(): List<Conversation> = conversationLocalDataSource.getConversations()

    override fun getConversationFlow(interlocutorId: String): Flow<Conversation> =
        conversationLocalDataSource.getConversationFlow(interlocutorId).filterNotNull()

    override suspend fun getConversation(interlocutorId: String): Conversation? =
        conversationLocalDataSource.getConversation(interlocutorId)

    override suspend fun getRemoteConversationsFlow(userId: String): Flow<ConversationDTO> =
        conversationRemoteDataSource.listenConversations(userId).map { it.toDTO(userId) }

    override suspend fun createLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun createRemoteConversation(conversation: Conversation, userId: String) {
        try {
            conversationRemoteDataSource.createConversation(conversation, userId)
        } catch (e: Exception) {
            e("Error creating remote conversation ${conversation.id}", e)
            throw e
        }
    }

    override suspend fun updateConversationEffectiveFrom(conversation: Conversation, currentUserId: String, effectiveFrom: LocalDateTime) {
        try {
            conversationRemoteDataSource.updateConversationEffectiveFrom(conversation.id, currentUserId, effectiveFrom)
            conversationLocalDataSource.updateConversationEffectiveFrom(conversation.id, effectiveFrom)
        } catch (e: Exception) {
            e("Error updating conversation effective from  ${conversation.id}", e)
            throw e
        }
    }

    override suspend fun updateLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.updateConversation(conversation)
    }

    override suspend fun upsertLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun deleteConversation(conversationId: String, currentUserId: String, deleteTime: LocalDateTime) {
        try {
            conversationRemoteDataSource.updateConversationEffectiveFrom(conversationId, currentUserId, deleteTime)
            conversationLocalDataSource.deleteConversation(conversationId)
        } catch (e: Exception) {
            e("Error deleting remote conversation $conversationId", e)
            throw e
        }
    }

    override suspend fun deleteLocalConversations() {
        conversationLocalDataSource.deleteConversations()
    }
}