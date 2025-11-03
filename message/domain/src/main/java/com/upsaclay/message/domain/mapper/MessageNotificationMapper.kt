package com.upsaclay.message.domain.mapper

import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.entity.MessageNotificationUi

fun List<MessageNotification>.toNotificationsUi(): List<MessageNotificationUi> {
   return groupBy { it.conversation }
       .map { (conversation, messages) ->
           MessageNotificationUi(
               conversation = conversation,
               messages = messages.map {
                   MessageNotificationUi.Message(
                       text = it.message.content,
                       timestamp = it.message.timestamp
                   )
               }
           )
       }
}