package com.upsaclay.message.data.local

import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.mapper.toConversation
import com.upsaclay.message.data.mapper.toLocal
import com.upsaclay.message.domain.entity.Conversation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class ConversationLocalDataSource(
    private val conversationDao: ConversationDao
) {
    suspend fun getConversations(): List<Conversation> =
        conversationDao.getConversations().map { it.toConversation() }

    fun getConversationFlow(interlocutorId: String): Flow<Conversation?> =
        conversationDao.getConversationFlow(interlocutorId).map { it?.toConversation() }

    suspend fun getConversation(interlocutorId: String): Conversation? =
        conversationDao.getConversation(interlocutorId)?.toConversation()

    suspend fun updateConversationEffectiveFrom(conversationId: String, effectiveFrom: LocalDateTime) {
        withContext(Dispatchers.IO) {
            conversationDao.updateConversationEffectiveFrom(conversationId, effectiveFrom.toEpochMilliUTC())
        }
    }

    suspend fun updateConversation(conversation: Conversation) {
        withContext(Dispatchers.IO) {
            conversationDao.updateConversation(conversation.toLocal())
        }
    }

    suspend fun upsertConversation(conversation: Conversation) {
        withContext(Dispatchers.IO) {
            conversationDao.upsertConversation(conversation.toLocal())
        }
    }

    suspend fun deleteConversation(conversationId: String) {
        withContext(Dispatchers.IO) {
            conversationDao.deleteConversation(conversationId)
        }
    }

    suspend fun deleteConversations() {
        withContext(Dispatchers.IO) {
            conversationDao.deleteConversations()
        }
    }
}