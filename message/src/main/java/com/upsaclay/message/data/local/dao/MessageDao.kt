package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.remote.MessageField
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query(
        "SELECT * FROM messages " +
            "WHERE ${MessageField.CONVERSATION_ID} = :conversationId " +
            "ORDER BY timestamp DESC " +
            "LIMIT 10"
    )
    fun getLastMessages(conversationId: String): Flow<List<LocalMessage>>

    @Query(
        "SELECT * FROM messages " +
            "WHERE ${MessageField.CONVERSATION_ID} = :conversationId " +
            "ORDER BY timestamp DESC " +
            "LIMIT 10 " +
            "OFFSET :offset"
    )
    suspend fun getMessages(conversationId: String, offset: Int): List<LocalMessage>
}