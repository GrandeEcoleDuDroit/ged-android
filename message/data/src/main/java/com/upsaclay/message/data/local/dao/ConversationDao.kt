package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME")
    suspend fun getConversations(): List<LocalConversation>

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME WHERE $INTERLOCUTOR_ID = :interlocutorId")
    fun getConversationFlow(interlocutorId: String): Flow<LocalConversation?>

    @Query("SELECT * FROM $CONVERSATIONS_TABLE_NAME WHERE $INTERLOCUTOR_ID = :interlocutorId")
    suspend fun getConversation(interlocutorId: String): LocalConversation?

    @Update
    suspend fun updateConversation(localConversation: LocalConversation)

    @Upsert
    suspend fun upsertConversation(localConversation: LocalConversation)

    @Query("DELETE FROM $CONVERSATIONS_TABLE_NAME")
    suspend fun deleteConversations()

    @Query("DELETE FROM $CONVERSATIONS_TABLE_NAME WHERE $CONVERSATION_ID = :conversationId")
    suspend fun deleteConversation(conversationId: String)
}