package com.upsaclay.message.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.upsaclay.message.data.remote.MessageField

data class RemoteMessage(
    @get:PropertyName(MessageField.MESSAGE_ID)
    @set:PropertyName(MessageField.MESSAGE_ID)
    var messageId: String = "",

    @get:PropertyName(MessageField.CONVERSATION_ID)
    @set:PropertyName(MessageField.CONVERSATION_ID)
    var conversationId: String = "",

    @get:PropertyName("sender_id")
    @set:PropertyName("sender_id")
    var senderId: String = "",

    @get:PropertyName("text")
    @set:PropertyName("text")
    var text: String = "",

    @get:PropertyName("timestamp")
    @set:PropertyName("timestamp")
    var timestamp: Timestamp = Timestamp.now(),

    @get:PropertyName("is_read")
    @set:PropertyName("is_read")
    var isRead: Boolean = false,

    @get:PropertyName("type")
    @set:PropertyName("type")
    var type: String = ""
)
