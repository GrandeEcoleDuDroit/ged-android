package com.upsaclay.message.data.model

internal object MessageField {
    const val TABLE_NAME = "messages"
    const val MESSAGE_ID = "messageId"
    const val CONVERSATION_ID = "conversationId"
    const val SENDER_ID = "senderId"
    const val RECIPIENT_ID = "recipientId"
    const val CONTENT = "content"
    const val TIMESTAMP = "timestamp"
    const val SEEN = "seen"

    object Local {
        const val STATE = "messageState"
    }
}