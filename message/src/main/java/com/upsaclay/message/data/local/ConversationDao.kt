package com.upsaclay.message.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations")
    fun getAllConversationsFlow(): Flow<List<LocalConversation>>

    @Query("SELECT * FROM conversations")
    fun getAllConversations(): List<LocalConversation>

    @Upsert
    suspend fun upsertConversation(localConversation: LocalConversation)
}