package com.upsaclay.message.data.model

import java.time.LocalDateTime

internal data class MessageDTO(
    val messageId: String,
    val senderId: Int,
    val conversationId: String,
    val text: String,
    val date: LocalDateTime,
    val isRead: Boolean,
    val type: String
)
