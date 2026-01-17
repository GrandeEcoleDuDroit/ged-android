package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.formatUrl
import com.upsaclay.common.domain.UserUtils
import com.upsaclay.common.domain.entity.SchoolLevel
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
import com.upsaclay.message.data.local.model.LocalMessageNotification
import com.upsaclay.message.data.remote.RemoteMessageNotification
import com.upsaclay.message.domain.MessageNotificationUtils
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.MessageNotification

fun MessageNotification.toLocal() = LocalMessageNotification(
    messageId = message.messageId,
    messageContent = message.content,
    messageTimestamp = message.timestamp,
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

fun MessageNotification.toRemote(currentUser: User) = RemoteMessageNotification(
    conversation = RemoteMessageNotification.Conversation(
        id = conversation.id,
        interlocutor = RemoteMessageNotification.Conversation.Interlocutor(
            id = currentUser.id,
            firstName = currentUser.firstName,
            lastName = currentUser.lastName,
            fullName = currentUser.fullName,
            email = currentUser.email,
            schoolLevel = currentUser.schoolLevel.number,
            admin = currentUser.admin,
            profilePictureFileName = UserUtils.ProfilePicture.getFileName(currentUser.profilePictureUrl),
            state = currentUser.state.number,
            tester = currentUser.tester
        ),
        createdAt = conversation.createdAt.toEpochMilliUTC(),
        deleteTime = conversation.effectiveFrom?.toEpochMilliUTC()
    ),
    messageId = message.messageId,
    content = message.content,
    timestamp = message.timestamp
)

fun LocalMessageNotification.toMessageNotification() = MessageNotification(
    conversation = toConversation(),
    message = MessageNotification.Message(
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
        interlocutor = User(
            id = conversation.interlocutor.id,
            firstName = conversation.interlocutor.firstName,
            lastName = conversation.interlocutor.lastName,
            email = conversation.interlocutor.email,
            schoolLevel = SchoolLevel.fromNumber(conversation.interlocutor.schoolLevel),
            admin = conversation.interlocutor.admin,
            profilePictureUrl = UserUtils.ProfilePicture.formatUrl(conversation.interlocutor.profilePictureFileName),
            state = User.UserState.fromNumber(conversation.interlocutor.state),
            tester = conversation.interlocutor.tester
        ),
        createdAt = conversation.createdAt.toLocalDateTimeUTC(),
        state = Conversation.ConversationState.CREATED,
        effectiveFrom = conversation.deleteTime?.toLocalDateTimeUTC()
    ),
    message = MessageNotification.Message(
        messageId = messageId,
        content = content,
        timestamp = timestamp
    )
)

fun RemoteMessageNotification.toFcm() = FcmMessage(
    notification = FcmNotification(
        title = conversation.interlocutor.fullName,
        body = content
    ),
    data = FcmData(
        type = FcmDataType.MESSAGE,
        value = this
    ),
    android = AndroidConfig(
        notification = AndroidNotification(
            channelId = MessageNotificationUtils.CHANNEL_ID,
        )
    ),
    apns = ApnsConfig(
        headers = ApnsHeaders(
            apnsCollapseId = MessageNotificationUtils.formatNotificationId(conversation.id)
        ),
        payload = ApnsPayload(
            aps = Aps(
                alert = Alert(
                    title = conversation.interlocutor.fullName,
                    body = content
                )
            )
        )
    )
)