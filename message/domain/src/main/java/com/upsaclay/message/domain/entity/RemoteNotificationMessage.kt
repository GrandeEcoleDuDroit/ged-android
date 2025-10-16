package com.upsaclay.message.domain.entity

data class RemoteNotificationMessage(
    val conversation: RemoteNotificationMessage.Conversation,
    val message: NotificationMessage.MessageContent
) {
    data class Conversation(
        val id: String,
        val interlocutor: Conversation.Interlocutor,
        val createdAt: Long,
        val deleteTime: Long? = null
    ) {
        data class Interlocutor(
            val id: String,
            val firstName: String,
            val lastName: String,
            val fullName: String,
            val email: String,
            val schoolLevel: String,
            val isMember: Boolean = false,
            val profilePictureFileName: String? = null,
            val isDeleted: Boolean = false
        )
    }
}