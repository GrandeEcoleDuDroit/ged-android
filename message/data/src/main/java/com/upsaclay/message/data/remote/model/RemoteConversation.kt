package com.upsaclay.message.data.remote.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import com.upsaclay.message.data.model.ConversationField.Remote.CONVERSATION_ID
import com.upsaclay.message.data.model.ConversationField.Remote.CREATED_AT
import com.upsaclay.message.data.model.ConversationField.Remote.EFFECTIVE_FROM
import com.upsaclay.message.data.model.ConversationField.Remote.PARTICIPANTS

internal data class RemoteConversation(
    @get:PropertyName(CONVERSATION_ID)
    @set:PropertyName(CONVERSATION_ID)
    var conversationId: String = "",

    @get:PropertyName(PARTICIPANTS)
    @set:PropertyName(PARTICIPANTS)
    var participants: List<String> = emptyList(),

    @get:PropertyName(CREATED_AT)
    @set:PropertyName(CREATED_AT)
    var createdAt: Timestamp = Timestamp.now(),

    @get:PropertyName(EFFECTIVE_FROM)
    @set:PropertyName(EFFECTIVE_FROM)
    var effectiveFrom: Map<String, Timestamp>? = null
)