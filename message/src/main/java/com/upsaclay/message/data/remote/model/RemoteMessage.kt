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

    @get:PropertyName(MessageField.SENDER_ID)
    @set:PropertyName(MessageField.SENDER_ID)
    var senderId: String = "",

    @get:PropertyName(MessageField.TEXT)
    @set:PropertyName(MessageField.TEXT)
    var text: String = "",

    @get:PropertyName(MessageField.TIMESTAMP)
    @set:PropertyName(MessageField.TIMESTAMP)
    var timestamp: Timestamp = Timestamp.now(),

    @get:PropertyName(MessageField.IS_READ)
    @set:PropertyName(MessageField.IS_READ)
    var isRead: Boolean = false,

    @get:PropertyName(MessageField.TYPE)
    @set:PropertyName(MessageField.TYPE)
    var type: String = ""
)
