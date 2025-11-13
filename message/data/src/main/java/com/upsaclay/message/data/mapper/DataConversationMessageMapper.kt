package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.UrlUtils
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.User.UserState
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.common.domain.extensions.uppercaseFirstLetter
import com.upsaclay.message.data.local.model.LocalConversationMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState

fun LocalConversationMessage.toConversationMessage() = ConversationMessage(
    conversation = this.toConversation(),
    lastMessage = this.toMessage()
)

private fun LocalConversationMessage.toConversation() = Conversation(
    id = conversationId,
    interlocutor = User(
        id = conversationInterlocutorId,
        firstName = conversationInterlocutorFirstName.uppercaseFirstLetter(),
        lastName = conversationInterlocutorLastName.uppercaseFirstLetter(),
        email = conversationInterlocutorEmail,
        schoolLevel = SchoolLevel.fromNumber(conversationInterlocutorSchoolLevel),
        admin = conversationInterlocutorAdmin,
        profilePictureUrl = UrlUtils.formatOracleBucketUrl(conversationInterlocutorProfilePictureFileName),
        state = UserState.fromString(conversationInterlocutorState),
        tester = conversationInterlocutorTester
    ),
    createdAt = conversationCreatedAt.toLocalDateTimeUTC(),
    state = Conversation.ConversationState.valueOf(conversationState),
    deleteTime = conversationDeleteTime?.toLocalDateTimeUTC()
)

private fun LocalConversationMessage.toMessage() = Message(
    id = messageId,
    senderId = messageSenderId,
    recipientId = messageRecipientId,
    conversationId = conversationId,
    content = messageContent,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = messageSeen,
    state = MessageState.valueOf(messageState)
)

