package com.upsaclay.message.domain.mapper

import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.entity.MessagesNotification

fun List<MessageNotification>.toMessagesNotification(): List<MessagesNotification> {
    return groupBy { it.conversation }
        .map { (conversation, messagesNotification) ->
            MessagesNotification(
                conversation = conversation,
                messages = messagesNotification.map { it.messageContent }
            )
        }
}