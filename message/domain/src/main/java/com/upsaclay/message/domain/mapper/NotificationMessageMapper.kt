package com.upsaclay.message.domain.mapper

import com.upsaclay.common.domain.entity.FcmAndroid
import com.upsaclay.common.domain.entity.FcmAndroidNotification
import com.upsaclay.common.domain.entity.FcmData
import com.upsaclay.common.domain.entity.FcmDataType
import com.upsaclay.common.domain.entity.FcmMessage
import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessages
import com.upsaclay.message.domain.usecase.MESSAGE_CHANNEL_NOTIFICATION_ID

fun List<NotificationMessage>.toNotificationMessages(): List<NotificationMessages> {
    return groupBy { it.conversation }
        .map { (conversation, notificationMessages) ->
            NotificationMessages(
                conversation = conversation,
                messages = notificationMessages.map { it.message }
            )
        }
}

fun NotificationMessage.toFcm(user: User) = FcmMessage(
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = NotificationMessage(
            conversation = conversation.copy(interlocutor = user),
            message = message
        )
    ),
    android = FcmAndroid(
        notification = FcmAndroidNotification(
            channelId = MESSAGE_CHANNEL_NOTIFICATION_ID
        )
    )
)
