package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.local.model.LocalConversation
import kotlinx.coroutines.flow.Flow

internal class ConversationLocalDataSource(private val conversationDao: ConversationDao) {
    fun getAllConversations(): Flow<List<LocalConversation>> = conversationDao.getAllConversationsFlow()

    suspend fun insertConversation(localConversation: LocalConversation) {
        conversationDao.insertConversation(localConversation)
    }
}