package com.upsaclay.message.domain.entity

data class MessageNotification(
    val conversation: Conversation,
    val messageContent: MessageNotification.MessageContent
) {
    data class MessageContent(
        val content: String,
        val date: Long,
    )
}

data class MessagesNotification(
    val conversation: Conversation,
    val messages: List<MessageNotification.MessageContent>
)