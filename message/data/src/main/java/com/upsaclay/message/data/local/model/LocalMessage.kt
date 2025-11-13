package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONTENT
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SEEN
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SENDER_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_STATE
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_TIMESTAMP
import com.upsaclay.message.data.model.MessageField.MESSAGE_TABLE_NAME

@Entity(tableName = MESSAGE_TABLE_NAME)
data class LocalMessage(
    @PrimaryKey
    @ColumnInfo(name = MESSAGE_ID)
    val messageId: String,
    @ColumnInfo(name = MESSAGE_SENDER_ID)
    val messageSenderId: String,
    @ColumnInfo(name = MESSAGE_RECIPIENT_ID)
    val messageRecipientId: String,
    @ColumnInfo(name = MESSAGE_CONVERSATION_ID)
    val messageConversationId: String,
    @ColumnInfo(name = MESSAGE_CONTENT)
    val messageContent: String,
    @ColumnInfo(name = MESSAGE_TIMESTAMP)
    val messageTimestamp: Long,
    @ColumnInfo(name = MESSAGE_SEEN)
    val messageSeen: Boolean,
    @ColumnInfo(name = MESSAGE_STATE)
    val messageState: String
)