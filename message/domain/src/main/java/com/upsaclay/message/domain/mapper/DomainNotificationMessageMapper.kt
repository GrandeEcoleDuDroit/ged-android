package com.upsaclay.message.domain.mapper

import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessages

fun List<NotificationMessage>.toNotificationMessages(): List<NotificationMessages> {
    return groupBy { it.conversation }
        .map { (conversation, notificationMessages) ->
            NotificationMessages(
                conversation = conversation,
                messages = notificationMessages.map { it.messageContent }
            )
        }
}