package com.upsaclay.message.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.data.model.ConversationField.CONVERSATION_TABLE_NAME
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Local.CONVERSATION_EFFECTIVE_FROM
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
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SEEN
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_SENDER_ID
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_STATE
import com.upsaclay.message.data.model.MessageField.Local.MESSAGE_TIMESTAMP
import com.upsaclay.message.data.model.MessageField.MESSAGE_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationMessageDao {
    @Transaction
    @Query("""
        SELECT C.$CONVERSATION_ID,
            C.$CONVERSATION_INTERLOCUTOR_ID, 
            C.$CONVERSATION_INTERLOCUTOR_FIRST_NAME,
            C.$CONVERSATION_INTERLOCUTOR_LAST_NAME, 
            C.$CONVERSATION_INTERLOCUTOR_EMAIL, 
            C.$CONVERSATION_INTERLOCUTOR_SCHOOL_LEVEL,
            C.$CONVERSATION_INTERLOCUTOR_ADMIN,
            C.$CONVERSATION_INTERLOCUTOR_PROFILE_PICTURE_FILE_NAME,
            C.$CONVERSATION_INTERLOCUTOR_STATE,
            C.$CONVERSATION_INTERLOCUTOR_TESTER,
            C.$CONVERSATION_CREATED_AT,
            C.$CONVERSATION_STATE, 
            C.$CONVERSATION_EFFECTIVE_FROM,
            M.$MESSAGE_ID, 
            M.$MESSAGE_SENDER_ID,
            M.$MESSAGE_RECIPIENT_ID,
            M.$MESSAGE_CONTENT,
            M.$MESSAGE_TIMESTAMP,
            M.$MESSAGE_SEEN, 
            M.$MESSAGE_STATE
        FROM $CONVERSATION_TABLE_NAME C
        JOIN $MESSAGE_TABLE_NAME M ON C.$CONVERSATION_ID = M.$MESSAGE_CONVERSATION_ID
        JOIN (
            SELECT $MESSAGE_CONVERSATION_ID, MAX($MESSAGE_TIMESTAMP) AS MAX_TIMESTAMP
            FROM $MESSAGE_TABLE_NAME
            GROUP BY $MESSAGE_CONVERSATION_ID
        ) M_MAX
          ON M.$MESSAGE_CONVERSATION_ID = M_MAX.$MESSAGE_CONVERSATION_ID
          AND M.$MESSAGE_TIMESTAMP = M_MAX.MAX_TIMESTAMP
          ORDER BY M_MAX.MAX_TIMESTAMP DESC
    """)
    fun getConversationsMessage(): Flow<List<LocalConversationMessage>>
}