package com.upsaclay.message.domain.model

import java.time.LocalDateTime

data class Message(
    val id: String = "",
    val senderId: Int,
    val text: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val isRead: Boolean = false,
    val isSent: Boolean = false,
    val type: MessageType
)