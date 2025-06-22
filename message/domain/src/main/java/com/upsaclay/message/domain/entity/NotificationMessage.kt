package com.upsaclay.message.domain.entity

data class NotificationMessage(
    val conversation: Conversation,
    val message: Message
)

data class NotificationMessages(
    val conversation: Conversation,
    val messages: List<Message>
)