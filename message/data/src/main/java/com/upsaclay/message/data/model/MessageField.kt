package com.upsaclay.message.data.model

internal object MessageField {
    const val MESSAGE_TABLE_NAME = "messages"

    object Local {
        const val MESSAGE_ID = "message_id"
        const val MESSAGE_CONVERSATION_ID = "message_conversation_id"
        const val MESSAGE_SENDER_ID = "message_sender_id"
        const val MESSAGE_RECIPIENT_ID = "message_recipient_id"
        const val MESSAGE_CONTENT = "message_content"
        const val MESSAGE_TIMESTAMP = "message_timestamp"
        const val MESSAGE_SEEN = "message_seen"
        const val MESSAGE_STATE = "message_state"
    }

    object Remote {
        const val MESSAGE_ID = "messageId"
        const val CONVERSATION_ID = "conversationId"
        const val SENDER_ID = "senderId"
        const val RECIPIENT_ID = "recipientId"
        const val CONTENT = "content"
        const val TIMESTAMP = "timestamp"
        const val SEEN = "seen"
        const val NOT_VISIBLE_FOR = "notVisibleFor"
    }
}