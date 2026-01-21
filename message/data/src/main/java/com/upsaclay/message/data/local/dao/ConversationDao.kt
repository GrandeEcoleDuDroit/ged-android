package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_EFFECTIVE_FROM
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_ID
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME")
    fun getConversationsFlow(): Flow<List<LocalConversation>>

    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME WHERE $CONVERSATION_INTERLOCUTOR_ID = :interlocutorId")
    fun getConversationFlow(interlocutorId: String): Flow<LocalConversation?>

    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME")
    suspend fun getConversations(): List<LocalConversation>

    @Query("SELECT * FROM $CONVERSATION_TABLE_NAME WHERE $CONVERSATION_INTERLOCUTOR_ID = :interlocutorId")
    suspend fun getConversation(interlocutorId: String): LocalConversation?

    @Update
    suspend fun updateConversation(localConversation: LocalConversation)

    @Query("""
        UPDATE $CONVERSATION_TABLE_NAME
        SET $CONVERSATION_EFFECTIVE_FROM = :effectiveFrom
        WHERE $CONVERSATION_ID = :conversationId
    """)
    suspend fun updateConversationEffectiveFrom(conversationId: String, effectiveFrom: Long)

    @Upsert
    suspend fun upsertConversation(localConversation: LocalConversation)

    @Query("DELETE FROM $CONVERSATION_TABLE_NAME")
    suspend fun deleteConversations()

    @Query("DELETE FROM $CONVERSATION_TABLE_NAME WHERE $CONVERSATION_ID = :conversationId")
    suspend fun deleteConversation(conversationId: String)
}