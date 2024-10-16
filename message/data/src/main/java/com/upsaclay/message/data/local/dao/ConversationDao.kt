package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.upsaclay.message.data.local.model.LocalConversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversations")
    fun getAllConversationsFlow(): Flow<List<LocalConversation>>

    @Query("SELECT * FROM conversations")
    fun getAllConversations(): List<LocalConversation>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConversation(localConversation: LocalConversation)
}