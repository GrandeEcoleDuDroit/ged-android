package com.upsaclay.message.domain.entity

data class NotificationMessage(
    val conversation: Conversation,
    val messageContent: NotificationMessage.MessageContent
) {
    data class MessageContent(
        val content: String,
        val date: Long,
    )
}

data class NotificationMessages(
    val conversation: Conversation,
    val messages: List<NotificationMessage.MessageContent>
)