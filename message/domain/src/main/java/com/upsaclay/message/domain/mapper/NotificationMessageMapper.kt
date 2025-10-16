package com.upsaclay.message.domain.mapper

import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.fcm.Alert
import com.upsaclay.common.domain.entity.fcm.AndroidConfig
import com.upsaclay.common.domain.entity.fcm.AndroidNotification
import com.upsaclay.common.domain.entity.fcm.ApnsConfig
import com.upsaclay.common.domain.entity.fcm.ApnsHeaders
import com.upsaclay.common.domain.entity.fcm.ApnsPayload
import com.upsaclay.common.domain.entity.fcm.Aps
import com.upsaclay.common.domain.entity.fcm.FcmData
import com.upsaclay.common.domain.entity.fcm.FcmDataType
import com.upsaclay.common.domain.entity.fcm.FcmMessage
import com.upsaclay.common.domain.entity.fcm.FcmNotification
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.domain.NotificationMessageUtils
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessages
import com.upsaclay.message.domain.entity.RemoteNotificationMessage

fun List<NotificationMessage>.toNotificationMessages(): List<NotificationMessages> {
    return groupBy { it.conversation }
        .map { (conversation, notificationMessages) ->
            NotificationMessages(
                conversation = conversation,
                messages = notificationMessages.map { it.messageContent }
            )
        }
}

fun NotificationMessage.toRemote(currentUser: User) = RemoteNotificationMessage(
    conversation = RemoteNotificationMessage.Conversation(
        id = conversation.id,
        interlocutor = currentUser.toInterlocutor(),
        createdAt = conversation.createdAt.toEpochMilliUTC(),
        deleteTime = conversation.deleteTime?.toEpochMilliUTC()
    ),
    message = messageContent
)

fun RemoteNotificationMessage.toNotificationMessage() = NotificationMessage(
    conversation = Conversation(
        id = conversation.id,
        interlocutor = conversation.interlocutor.toUser(),
        createdAt = conversation.createdAt.toLocalDateTimeUTC(),
        state = ConversationState.CREATED,
        deleteTime = conversation.deleteTime?.toLocalDateTimeUTC()
    ),
    messageContent = message
)

fun RemoteNotificationMessage.toFcm() = FcmMessage(
    notification = FcmNotification(
        title = conversation.interlocutor.fullName,
        body = message.content
    ),
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = this
    ),
    android = AndroidConfig(
        notification = AndroidNotification(
            channelId = NotificationMessageUtils.CHANNEL_ID,
        )
    ),
    apns = ApnsConfig(
        headers = ApnsHeaders(
            apnsCollapseId = NotificationMessageUtils.formatNotificationId(conversation.id)
        ),
        payload = ApnsPayload(
            aps = Aps(
                alert = Alert(
                    title = conversation.interlocutor.fullName,
                    body = message.content
                )
            )
        )
    )
)

private fun User.toInterlocutor() = RemoteNotificationMessage.Conversation.Interlocutor(
    id = id,
    firstName = firstName,
    lastName = lastName,
    fullName = fullName,
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureFileName = UrlUtils.extractFileName(profilePictureUrl),
    isDeleted = isDeleted
)

private fun RemoteNotificationMessage.Conversation.Interlocutor.toUser() = User(
    id = id,
    firstName = firstName,
    lastName = lastName,
    email = email,
    schoolLevel = schoolLevel,
    isMember = isMember,
    profilePictureUrl = UrlUtils.formatOracleBucketUrl(profilePictureFileName),
    isDeleted = isDeleted
)