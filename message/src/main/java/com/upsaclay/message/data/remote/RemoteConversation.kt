package com.upsaclay.message.data.remote

import com.google.firebase.firestore.PropertyName

internal data class RemoteConversation(
    @get:PropertyName("conversation_id")
    @set:PropertyName("conversation_id")
    var conversationId: String = "",

    @get:PropertyName("participants")
    @set:PropertyName("participants")
    var participants: List<String> = emptyList(),

    @get:PropertyName("last_message")
    @set:PropertyName("last_message")
    var lastMessage: RemoteMessage? = null
)
