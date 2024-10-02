package com.upsaclay.message.data.local

import com.upsaclay.message.data.local.dao.ConversationDao
import com.upsaclay.message.data.local.model.LocalConversation
import kotlinx.coroutines.flow.Flow

internal class ConversationLocalDataSource(
    private val conversationDao: ConversationDao
) {
    fun getAllConversationsFlow(): Flow<List<LocalConversation>> =
        conversationDao.getAllConversationsFlow()

    fun getAllConversations(): List<LocalConversation> =
        conversationDao.getAllConversations()

   suspend fun upsertConversation(localConversation: LocalConversation) {
        conversationDao.upsertConversation(localConversation)
   }
}