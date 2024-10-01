package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.CONVERSATIONS_TABLE_NAME
import com.upsaclay.message.data.remote.ConversationField


@Entity(tableName = CONVERSATIONS_TABLE_NAME)
data class LocalConversation(
    @PrimaryKey
    @ColumnInfo(name = ConversationField.CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = ConversationField.PARTICIPANTS)
    val participants: String
)