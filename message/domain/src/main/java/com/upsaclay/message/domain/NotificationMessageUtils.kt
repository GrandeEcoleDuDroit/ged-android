package com.upsaclay.message.domain

import java.util.UUID

object NotificationMessageUtils {
    const val CHANNEL_ID = "message_channel_notification_id"

    fun formatNotificationId(conversationId: String): String {
        val notificationIdPrefix = formatNotificationIdPrefix(conversationId)
        val uuidShort = UUID.randomUUID().toString().take(6)
        val timestamp = System.currentTimeMillis().toInt()
        return "${notificationIdPrefix}_${timestamp}_$uuidShort"
    }

    private fun formatNotificationIdPrefix(conversationId: String): String {
        val parts = conversationId.split("_")
        val part1 = parts.firstOrNull()?.take(20) ?: ""
        val part2 = parts.lastOrNull()?.take(20) ?: ""
        return "${part1}_${part2}"
    }
}