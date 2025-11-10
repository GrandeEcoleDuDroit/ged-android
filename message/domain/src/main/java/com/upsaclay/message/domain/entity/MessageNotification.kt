package com.upsaclay.message.domain.entity

data class MessageNotification(
    val conversation: Conversation,
    val message: MessageNotification.Message
) {
    data class Message(
        val messageId: String,
        val content: String,
        val timestamp: Long
    )
}

data class MessageNotificationUi(
    val conversation: Conversation,
    val messages: List<MessageNotificationUi.Message>
) {
    data class Message(
        val text: String,
        val timestamp: Long
    )
}