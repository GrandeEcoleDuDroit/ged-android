package com.upsaclay.message.data.remote.model

import com.upsaclay.common.data.remote.model.OracleUser

data class RemoteMessageNotification(
    val conversation: RemoteMessageNotification.NotificationConversation,
    val messageId: String,
    val content: String,
    val timestamp: Long
) {
    data class NotificationConversation(
        val id: String,
        val interlocutor: OracleUser,
        val createdAt: Long,
        val effectiveFrom: Long? = null
    )
}