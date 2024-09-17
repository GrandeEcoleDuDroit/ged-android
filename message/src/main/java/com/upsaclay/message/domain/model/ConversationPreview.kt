package com.upsaclay.message.domain.model

import com.upsaclay.common.domain.model.User

data class ConversationPreview(
    val id: Int,
    val interlocutor: User,
    val lastMessage: Message,
    val isRead: Boolean
)
