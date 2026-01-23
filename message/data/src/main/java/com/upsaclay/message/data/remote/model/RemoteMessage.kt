package com.upsaclay.message.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.upsaclay.message.data.model.MessageField.Remote.CONTENT
import com.upsaclay.message.data.model.MessageField.Remote.CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Remote.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.Remote.NOT_VISIBLE_FOR
import com.upsaclay.message.data.model.MessageField.Remote.RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Remote.SEEN
import com.upsaclay.message.data.model.MessageField.Remote.SENDER_ID
import com.upsaclay.message.data.model.MessageField.Remote.TIMESTAMP

internal data class RemoteMessage(
    @get:PropertyName(MESSAGE_ID)
    @set:PropertyName(MESSAGE_ID)
    var messageId: String = "",

    @get:PropertyName(CONVERSATION_ID)
    @set:PropertyName(CONVERSATION_ID)
    var conversationId: String = "",

    @get:PropertyName(SENDER_ID)
    @set:PropertyName(SENDER_ID)
    var senderId: String = "",

    @get:PropertyName(RECIPIENT_ID)
    @set:PropertyName(RECIPIENT_ID)
    var recipientId: String = "",

    @get:PropertyName(CONTENT)
    @set:PropertyName(CONTENT)
    var content: String = "",

    @get:PropertyName(TIMESTAMP)
    @set:PropertyName(TIMESTAMP)
    var timestamp: Timestamp = Timestamp.now(),

    @get:PropertyName(SEEN)
    @set:PropertyName(SEEN)
    var seen: Boolean = false,

    @get:PropertyName(NOT_VISIBLE_FOR)
    @set:PropertyName(NOT_VISIBLE_FOR)
    var notVisibleFor: Map<String, Boolean>? = null
)