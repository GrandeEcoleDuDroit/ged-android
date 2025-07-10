package com.upsaclay.message.domain.entity

import kotlinx.serialization.InternalSerializationApi

@OptIn(InternalSerializationApi::class)
data class ConversationMessage(
    val conversation: Conversation,
    val lastMessage: Message
)