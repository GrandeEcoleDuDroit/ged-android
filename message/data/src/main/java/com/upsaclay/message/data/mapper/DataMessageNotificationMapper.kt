package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.UrlUtils
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
    content = message.content,
    messageTimestamp = message.timestamp,
    conversationId = conversation.id,
    interlocutorId = conversation.interlocutor.id,
    interlocutorFirstName = conversation.interlocutor.firstName,
    interlocutorLastName = conversation.interlocutor.lastName,
    interlocutorEmail = conversation.interlocutor.email,
    interlocutorSchoolLevel = conversation.interlocutor.schoolLevel.number,
    interlocutorAdmin = conversation.interlocutor.admin,
    interlocutorProfilePictureFileName = UrlUtils.extractFileName(conversation.interlocutor.profilePictureUrl),
    interlocutorState = conversation.interlocutor.state.toString(),
    interlocutorTester = conversation.interlocutor.tester,
    createdAt = conversation.createdAt.toEpochMilliUTC(),
    conversationState = conversation.state.name,
    conversationDeleteTime = conversation.deleteTime?.toEpochMilliUTC()
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
            profilePictureFileName = UrlUtils.extractFileName(currentUser.profilePictureUrl),
            state = currentUser.state.toString(),
            tester = currentUser.tester
        ),
        createdAt = conversation.createdAt.toEpochMilliUTC(),
        deleteTime = conversation.deleteTime?.toEpochMilliUTC()
    ),
    messageId = message.messageId,
    content = message.content,
    timestamp = message.timestamp
)

fun LocalMessageNotification.toMessageNotification() = MessageNotification(
    conversation = toConversation(),
    message = MessageNotification.Message(
        messageId = messageId,
        content = content,
        timestamp = messageTimestamp
    )
)

private fun LocalMessageNotification.toConversation() = Conversation(
    id = conversationId,
    interlocutor = User(
        id = interlocutorId,
        firstName = interlocutorFirstName,
        lastName = interlocutorLastName,
        email = interlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(interlocutorSchoolLevel),
        admin = interlocutorAdmin,
        profilePictureUrl = UrlUtils.formatOracleBucketUrl(interlocutorProfilePictureFileName),
        state = User.UserState.fromString(interlocutorState),
        tester = interlocutorTester
    ),
    createdAt = createdAt.toLocalDateTimeUTC(),
    state = Conversation.ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
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
            profilePictureUrl = UrlUtils.formatOracleBucketUrl(conversation.interlocutor.profilePictureFileName),
            state = User.UserState.fromString(conversation.interlocutor.state),
            tester = conversation.interlocutor.tester
        ),
        createdAt = conversation.createdAt.toLocalDateTimeUTC(),
        state = Conversation.ConversationState.CREATED,
        deleteTime = conversation.deleteTime?.toLocalDateTimeUTC()
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