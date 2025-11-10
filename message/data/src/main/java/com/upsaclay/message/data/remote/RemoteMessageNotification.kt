package com.upsaclay.message.data.remote

data class RemoteMessageNotification(
    val conversation: Conversation,
    val messageId: String,
    val content: String,
    val timestamp: Long
) {
    data class Conversation(
        val id: String,
        val interlocutor: Interlocutor,
        val createdAt: Long,
        val deleteTime: Long? = null
    ) {
        data class Interlocutor(
            val id: String,
            val firstName: String,
            val lastName: String,
            val fullName: String,
            val email: String,
            val schoolLevel: Int,
            val admin: Boolean = false,
            val profilePictureFileName: String? = null,
            val state: String,
            val tester: Boolean = false
        )
    }
}