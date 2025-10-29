package com.upsaclay.message.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_DELETE_TIME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_STATE
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_EMAIL
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_FIRST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_ID
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_STATE
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_ADMIN
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_LAST_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_SCHOOL_LEVEL
import com.upsaclay.message.data.model.ConversationField.Local.INTERLOCUTOR_TESTER
import com.upsaclay.message.data.model.MessageField.CONTENT
import com.upsaclay.message.data.model.MessageField.TIMESTAMP

internal const val MESSAGE_NOTIFICATION_TABLE_NAME = "message_notification"

@Entity(tableName = MESSAGE_NOTIFICATION_TABLE_NAME)
data class LocalMessageNotification(
    @PrimaryKey
    @ColumnInfo(name = CONTENT)
    val content: String,
    @ColumnInfo(name = TIMESTAMP)
    val messageTimestamp: Long,
    @ColumnInfo(name = CONVERSATION_ID)
    val conversationId: String,
    @ColumnInfo(name = INTERLOCUTOR_ID)
    val interlocutorId: String,
    @ColumnInfo(name = INTERLOCUTOR_FIRST_NAME)
    val interlocutorFirstName: String,
    @ColumnInfo(name = INTERLOCUTOR_LAST_NAME)
    val interlocutorLastName: String,
    @ColumnInfo(name = INTERLOCUTOR_EMAIL)
    val interlocutorEmail: String,
    @ColumnInfo(name = INTERLOCUTOR_SCHOOL_LEVEL)
    val interlocutorSchoolLevel: Int,
    @ColumnInfo(name = INTERLOCUTOR_ADMIN)
    val interlocutorAdmin: Boolean,
    @ColumnInfo(name = INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME)
    val interlocutorProfilePictureFileName: String?,
    @ColumnInfo(name = INTERLOCUTOR_STATE)
    val interlocutorState: String,
    @ColumnInfo(name = INTERLOCUTOR_TESTER)
    val interlocutorTester: Boolean,
    @ColumnInfo(name = CREATED_AT)
    val createdAt: Long,
    @ColumnInfo(name = CONVERSATION_STATE)
    val conversationState: String,
    @ColumnInfo(name = CONVERSATION_DELETE_TIME)
    val conversationDeleteTime: Long?
)
