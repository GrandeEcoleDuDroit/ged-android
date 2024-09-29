package com.upsaclay.message.data.model

import java.time.LocalDateTime

data class MessageDTO(
    val messageId: String,
    val senderId: String,
    val conversationId: String,
    val text: String,
    val date: LocalDateTime,
    val isRead: Boolean,
    val type: String
)
