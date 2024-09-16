package com.upsaclay.message.domain.model

import com.upsaclay.common.domain.model.User

data class ConversationItemData(
    val id: Int,
    val interlocutor: User,
    val lastMessage: Message,
    val isRead: Boolean
)
