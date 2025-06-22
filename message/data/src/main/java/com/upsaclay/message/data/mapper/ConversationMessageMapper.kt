package com.upsaclay.message.data.mapper

import com.upsaclay.common.domain.UrlUtils
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState


fun LocalConversationMessage.toConversationMessage() = ConversationMessage(
    conversation = this.toConversation(),
    lastMessage = this.toMessage()
)

private fun LocalConversationMessage.toConversation() = Conversation(
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

private fun LocalConversationMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = seen,
    state = MessageState.valueOf(messageState)
)

