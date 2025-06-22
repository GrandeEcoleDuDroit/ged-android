package com.upsaclay.message.data.mapper

import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalNotificationMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.entity.NotificationMessages

fun LocalNotificationMessage.toNotificationMessage() = NotificationMessage(
    conversation = this.toConversation(),
    message = this.toMessage()
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
    messageId = message.id,
    senderId = message.senderId,
    recipientId = message.recipientId,
    content = message.content,
    messageTimestamp = message.date.toEpochMilliUTC(),
    seen = message.seen,
    messageState = message.state.name
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

private fun LocalNotificationMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = seen,
    state = MessageState.valueOf(messageState)
)

