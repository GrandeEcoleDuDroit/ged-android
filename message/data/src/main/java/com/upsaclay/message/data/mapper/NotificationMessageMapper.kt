package com.upsaclay.message.data.mapper

import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalNotificationMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.NotificationMessage

fun LocalNotificationMessage.toNotificationMessage() = NotificationMessage(
    conversation = toConversation(),
    messageContent = toMessageContent()
)

fun NotificationMessage.toLocal() = LocalNotificationMessage(
    conversationId = conversation.id,
    interlocutorId = conversation.interlocutor.id,
    interlocutorFirstName = conversation.interlocutor.firstName,
    interlocutorLastName = conversation.interlocutor.lastName,
    interlocutorEmail = conversation.interlocutor.email,
    interlocutorSchoolLevel = conversation.interlocutor.schoolLevel,
    interlocutorIsMember = conversation.interlocutor.isMember,
    interlocutorProfilePictureFileName = UrlUtils.getFileNameFromUrl(conversation.interlocutor.profilePictureUrl),
    createdAt = conversation.createdAt.toEpochMilliUTC(),
    conversationState = conversation.state.name,
    conversationDeleteTime = conversation.deleteTime?.toEpochMilliUTC(),
    content = messageContent.content,
    messageTimestamp = messageContent.date,
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
        profilePictureUrl = UrlUtils.formatProfilePictureUrl(interlocutorProfilePictureFileName)
    ),
    createdAt = createdAt.toLocalDateTimeUTC(),
    state = ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
)

private fun LocalNotificationMessage.toMessageContent() = NotificationMessage.MessageContent(
    content = content,
    date = messageTimestamp,
)

