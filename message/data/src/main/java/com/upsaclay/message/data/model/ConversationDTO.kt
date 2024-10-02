package com.upsaclay.message.data.model

internal data class ConversationDTO(
    val conversationId: String,
    val participants: List<Int>
)