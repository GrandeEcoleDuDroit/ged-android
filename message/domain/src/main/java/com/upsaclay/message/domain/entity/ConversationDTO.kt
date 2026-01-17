package com.upsaclay.message.domain.entity

import java.time.LocalDateTime

data class ConversationDTO(
    val conversationId: String,
    val participants: List<String>,
    val createdAt: LocalDateTime,
    val effectiveFrom: LocalDateTime?
)
