package com.upsaclay.message.domain.entity

import com.upsaclay.common.domain.entity.User
import kotlinx.serialization.InternalSerializationApi
import java.time.LocalDateTime

@OptIn(InternalSerializationApi::class)
data class ConversationUi(
    val id: String,
    val interlocutor: User,
    val lastMessage: Message,
    val createdAt: LocalDateTime,
    val state: ConversationState
)