package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_DELETE_TIME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_ADMIN
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_EMAIL
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_FIRST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_ID
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_LAST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_SCHOOL_LEVEL
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_STATE
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_INTERLOCUTOR_TESTER
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_STATE
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONTENT
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SEEN
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SENDER_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_STATE
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_TIMESTAMP

private const val CONVERSATION_MESSAGE_TABLE_NAME = "conversation_message"

@Entity(tableName = CONVERSATION_MESSAGE_TABLE_NAME)
data class LocalConversationMessage(
    @PrimaryKey
    @ColumnInfo(name = CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = CONVERSATION_CREATED_AT)
    val conversationCreatedAt: Long,
    @ColumnInfo(name = CONVERSATION_STATE)
    val conversationState: String,
    @ColumnInfo(name = CONVERSATION_DELETE_TIME)
    val conversationDeleteTime: Long?,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_ID)
    val conversationInterlocutorId: String,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_FIRST_NAME)
    val conversationInterlocutorFirstName: String,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_LAST_NAME)
    val conversationInterlocutorLastName: String,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_EMAIL)
    val conversationInterlocutorEmail: String,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_SCHOOL_LEVEL)
    val conversationInterlocutorSchoolLevel: Int,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_ADMIN)
    val conversationInterlocutorAdmin: Boolean,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME)
    val conversationInterlocutorProfilePictureFileName: String?,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_STATE)
    val conversationInterlocutorState: Int,
    @ColumnInfo(name = CONVERSATION_INTERLOCUTOR_TESTER)
    val conversationInterlocutorTester: Boolean,
    @ColumnInfo(name = MESSAGE_ID)
    val messageId: String,
    @ColumnInfo(name = MESSAGE_SENDER_ID)
    val messageSenderId: String,
    @ColumnInfo(name = MESSAGE_RECIPIENT_ID)
    val messageRecipientId: String,
    @ColumnInfo(name = MESSAGE_CONTENT)
    val messageContent: String,
    @ColumnInfo(name = MESSAGE_TIMESTAMP)
    val messageTimestamp: Long,
    @ColumnInfo(name = MESSAGE_SEEN)
    val messageSeen: Boolean,
    @ColumnInfo(name = MESSAGE_STATE)
    val messageState: String
)

