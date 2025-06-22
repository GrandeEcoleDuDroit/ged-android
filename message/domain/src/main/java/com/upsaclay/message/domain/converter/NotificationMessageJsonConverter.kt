package com.upsaclay.message.domain.converter

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.LocalDateTimeAdapter
import com.upsaclay.message.domain.entity.NotificationMessage
import java.time.LocalDateTime

object NotificationMessageJsonConverter {
    fun toNotificationMessage(notificationMessageJson: String): NotificationMessage? {
        return runCatching {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()
                .fromJson(notificationMessageJson, NotificationMessage::class.java)
        }.getOrNull()
    }

    fun toNotificationMessageJson(notificationMessage: NotificationMessage): String {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
            .create()
            .toJson(notificationMessage)
    }
}