package com.upsaclay.message.domain.model

import java.time.LocalDateTime

data class Message(val id: String, val senderId: Int, val text: String, val date: LocalDateTime, val isRead: Boolean, val type: String)