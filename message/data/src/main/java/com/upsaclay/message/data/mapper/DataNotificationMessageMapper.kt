package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.UrlUtils
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
import com.upsaclay.message.data.local.model.LocalNotificationMessage
import com.upsaclay.message.data.remote.RemoteNotificationMessage
import com.upsaclay.message.domain.NotificationMessageUtils
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.NotificationMessage

fun NotificationMessage.toLocal() = LocalNotificationMessage(
    conversationId = conversation.id,
    interlocutorId = conversation.interlocutor.id,
    interlocutorFirstName = conversation.interlocutor.firstName,
    interlocutorLastName = conversation.interlocutor.lastName,
    interlocutorEmail = conversation.interlocutor.email,
    interlocutorSchoolLevel = conversation.interlocutor.schoolLevel,
    interlocutorIsMember = conversation.interlocutor.isMember,
    interlocutorProfilePictureFileName = UrlUtils.extractFileName(conversation.interlocutor.profilePictureUrl),
    interlocutorIsDeleted = conversation.interlocutor.isDeleted,
    createdAt = conversation.createdAt.toEpochMilliUTC(),
    conversationState = conversation.state.name,
    conversationDeleteTime = conversation.deleteTime?.toEpochMilliUTC(),
    content = messageContent.content,
    messageTimestamp = messageContent.date
)

fun NotificationMessage.toRemote(currentUser: User) = RemoteNotificationMessage(
    conversation = RemoteNotificationMessage.Conversation(
        id = conversation.id,
        interlocutor = RemoteNotificationMessage.Conversation.Interlocutor(
            id = currentUser.id,
            firstName = currentUser.firstName,
            lastName = currentUser.lastName,
            fullName = currentUser.fullName,
            email = currentUser.email,
            schoolLevel = currentUser.schoolLevel,
            isMember = currentUser.isMember,
            profilePictureFileName = UrlUtils.extractFileName(currentUser.profilePictureUrl),
            isDeleted = currentUser.isDeleted
        ),
        createdAt = conversation.createdAt.toEpochMilliUTC(),
        deleteTime = conversation.deleteTime?.toEpochMilliUTC()
    ),
    message = messageContent
)

fun LocalNotificationMessage.toNotificationMessage() = NotificationMessage(
    conversation = toConversation(),
    messageContent = toMessage()
)

private fun LocalNotificationMessage.toConversation() = Conversation(
    id = conversationId,
    interlocutor = User(
        id = interlocutorId,
        firstName = interlocutorFirstName,
        lastName = interlocutorLastName,
        email = interlocutorEmail,
        schoolLevel = interlocutorSchoolLevel,
        isMember = interlocutorIsMember,
        profilePictureUrl = UrlUtils.formatOracleBucketUrl(interlocutorProfilePictureFileName),
        isDeleted = interlocutorIsDeleted
    ),
    createdAt = createdAt.toLocalDateTimeUTC(),
    state = ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
)

private fun LocalNotificationMessage.toMessage() = NotificationMessage.MessageContent(
    content = content,
    date = messageTimestamp,
)

fun RemoteNotificationMessage.toNotificationMessage() = NotificationMessage(
    conversation = Conversation(
        id = conversation.id,
        interlocutor = User(
            id = conversation.interlocutor.id,
            firstName = conversation.interlocutor.firstName,
            lastName = conversation.interlocutor.lastName,
            email = conversation.interlocutor.email,
            schoolLevel = conversation.interlocutor.schoolLevel,
            isMember = conversation.interlocutor.isMember,
            profilePictureUrl = UrlUtils.formatOracleBucketUrl(conversation.interlocutor.profilePictureFileName),
            isDeleted = conversation.interlocutor.isDeleted
        ),
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