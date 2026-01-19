package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.formatUrl
import com.upsaclay.common.data.toOracleUser
import com.upsaclay.common.data.toUser
import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.fcm.Alert
import com.upsaclay.common.domain.entity.fcm.AndroidConfig
import com.upsaclay.common.domain.entity.fcm.ApnsConfig
import com.upsaclay.common.domain.entity.fcm.ApnsHeaders
import com.upsaclay.common.domain.entity.fcm.ApnsPayload
import com.upsaclay.common.domain.entity.fcm.Aps
import com.upsaclay.common.domain.entity.fcm.FcmData
import com.upsaclay.common.domain.entity.fcm.FcmDataType
import com.upsaclay.common.domain.entity.fcm.FcmMessage
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalMessageNotification
import com.upsaclay.message.data.remote.model.RemoteMessageNotification
import com.upsaclay.message.domain.MessageNotificationUtils
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.MessageNotification

fun MessageNotification.toLocal() = LocalMessageNotification(
    messageId = messageContent.messageId,
    messageContent = messageContent.content,
    messageTimestamp = messageContent.timestamp,
    conversationId = conversation.id,
    conversationInterlocutorId = conversation.interlocutor.id,
    conversationInterlocutorFirstName = conversation.interlocutor.firstName,
    conversationInterlocutorLastName = conversation.interlocutor.lastName,
    conversationInterlocutorEmail = conversation.interlocutor.email,
    conversationInterlocutorSchoolLevel = conversation.interlocutor.schoolLevel.number,
    conversationInterlocutorAdmin = conversation.interlocutor.admin,
    conversationInterlocutorProfilePictureFileName = UserUtils.ProfilePicture.getFileName(conversation.interlocutor.profilePictureUrl),
    conversationInterlocutorState = conversation.interlocutor.state.number,
    conversationInterlocutorTester = conversation.interlocutor.tester,
    conversationCreatedAt = conversation.createdAt.toEpochMilliUTC(),
    conversationState = conversation.state.name,
    conversationDeleteTime = conversation.effectiveFrom?.toEpochMilliUTC()
)

fun MessageNotification.toRemote(currentUser: User) =
    RemoteMessageNotification(
        conversation = RemoteMessageNotification.NotificationConversation(
            id = conversation.id,
            interlocutor = currentUser.toOracleUser(),
            createdAt = conversation.createdAt.toEpochMilliUTC(),
            effectiveFrom = conversation.effectiveFrom?.toEpochMilliUTC()
        ),
        messageId = messageContent.messageId,
        content = messageContent.content,
        timestamp = messageContent.timestamp
    )

fun LocalMessageNotification.toMessageNotification() = MessageNotification(
    conversation = toConversation(),
    messageContent = MessageNotification.MessageContent(
        messageId = messageId,
        content = messageContent,
        timestamp = messageTimestamp
    )
)

private fun LocalMessageNotification.toConversation() = Conversation(
    id = conversationId,
    interlocutor = User(
        id = conversationInterlocutorId,
        firstName = conversationInterlocutorFirstName,
        lastName = conversationInterlocutorLastName,
        email = conversationInterlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(conversationInterlocutorSchoolLevel),
        admin = conversationInterlocutorAdmin,
        profilePictureUrl = UserUtils.ProfilePicture.formatUrl(conversationInterlocutorProfilePictureFileName),
        state = User.UserState.fromNumber(conversationInterlocutorState),
        tester = conversationInterlocutorTester
    ),
    createdAt = conversationCreatedAt.toLocalDateTimeUTC(),
    state = Conversation.ConversationState.valueOf(conversationState),
    effectiveFrom = conversationDeleteTime?.toLocalDateTimeUTC()
)

fun RemoteMessageNotification.toMessageNotification() = MessageNotification(
    conversation = Conversation(
        id = conversation.id,
        interlocutor = conversation.interlocutor.toUser(),
        createdAt = conversation.createdAt.toLocalDateTimeUTC(),
        state = Conversation.ConversationState.CREATED,
        effectiveFrom = conversation.effectiveFrom?.toLocalDateTimeUTC()
    ),
    messageContent = MessageNotification.MessageContent(
        messageId = messageId,
        content = content,
        timestamp = timestamp
    )
)

fun RemoteMessageNotification.toFcm() = FcmMessage(
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = this
    ),
    android = AndroidConfig(),
    apns = ApnsConfig(
        headers = ApnsHeaders(
            apnsCollapseId = MessageNotificationUtils.formatNotificationId(conversation.id)
        ),
        payload = ApnsPayload(
            aps = Aps(
                alert = Alert(
                    title = conversation.interlocutor.toUser().fullName,
                    body = content
                )
            )
        )
    )
)