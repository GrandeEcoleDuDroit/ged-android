package com.upsaclay.message.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SEEN
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_STATE
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_TIMESTAMP
import com.upsaclay.message.data.model.MessageField.MESSAGE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("""
        SELECT * FROM $MESSAGE_TABLE_NAME
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId 
        ORDER BY $MESSAGE_TIMESTAMP DESC
    """)
    fun getMessages(conversationId: String): PagingSource<Int, LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGE_TABLE_NAME
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId 
        ORDER BY $MESSAGE_TIMESTAMP DESC
        LIMIT 1
    """)
    fun getLastMessageFlow(conversationId: String): Flow<LocalMessage?>

    @Query("""
        SELECT * FROM $MESSAGE_TABLE_NAME
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId 
        ORDER BY $MESSAGE_TIMESTAMP DESC
        LIMIT 1
    """)
    suspend fun getLastMessage(conversationId: String): LocalMessage?

    @Query("""
        SELECT * FROM $MESSAGE_TABLE_NAME
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId 
        AND $MESSAGE_RECIPIENT_ID == :userId
        AND $MESSAGE_SEEN = 0
    """)
    suspend fun getUnreadMessagesByUser(conversationId: String, userId: String): List<LocalMessage>

    @Query("""
        SELECT * FROM $MESSAGE_TABLE_NAME
        WHERE $MESSAGE_STATE = 'SENDING'
    """)
    suspend fun getUnsentMessages(): List<LocalMessage>

    @Update
    suspend fun updateMessage(localMessage: LocalMessage)

    @Query("""
        UPDATE $MESSAGE_TABLE_NAME
        SET $MESSAGE_SEEN = 1
        WHERE $MESSAGE_CONVERSATION_ID = :conversationId
        AND $MESSAGE_RECIPIENT_ID = :userId
        AND $MESSAGE_SEEN = 0
    """)
    suspend fun updateSeenMessages(conversationId: String, userId: String)

    @Upsert
    suspend fun upsertMessage(localMessage: LocalMessage)

    @Delete
    suspend fun deleteMessage(localMessage: LocalMessage)

    @Query("DELETE FROM $MESSAGE_TABLE_NAME WHERE $MESSAGE_CONVERSATION_ID = :conversationId")
    suspend fun deleteMessages(conversationId: String)

    @Query("DELETE FROM $MESSAGE_TABLE_NAME")
    suspend fun deleteAllMessages()
}