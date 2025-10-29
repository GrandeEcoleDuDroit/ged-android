package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.MessageField.CONTENT
import com.upsaclay.message.data.model.MessageField.CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.STATE
import com.upsaclay.message.data.model.MessageField.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.SEEN
import com.upsaclay.message.data.model.MessageField.SENDER_ID
import com.upsaclay.message.data.model.MessageField.TABLE_NAME
import com.upsaclay.message.data.model.MessageField.TIMESTAMP

@Entity(tableName = TABLE_NAME)
data class LocalMessage(
    @PrimaryKey
    @ColumnInfo(name = MESSAGE_ID)
    val messageId: Long,
    @ColumnInfo(name = SENDER_ID)
    val senderId: String,
    @ColumnInfo(name = RECIPIENT_ID)
    val recipientId: String,
    @ColumnInfo(name = CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = CONTENT)
    val content: String,
    @ColumnInfo(name = TIMESTAMP)
    val messageTimestamp: Long,
    @ColumnInfo(name = SEEN)
    val seen: Boolean,
    @ColumnInfo(name = STATE)
    val state: String
)