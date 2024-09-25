package com.upsaclay.message.data.local

import kotlinx.coroutines.flow.Flow

class ConversationLocalDataSource(
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