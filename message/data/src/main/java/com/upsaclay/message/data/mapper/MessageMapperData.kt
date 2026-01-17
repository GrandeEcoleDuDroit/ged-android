package com.upsaclay.message.data.mapper

import com.upsaclay.common.data.extensions.toLocalDateTime
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.extensions.toLocalDateTimeUTC
import com.upsaclay.message.data.local.model.LocalMessage
import com.upsaclay.message.data.model.MessageField.Remote.CONTENT
import com.upsaclay.message.data.model.MessageField.Remote.CONVERSATION_ID
import com.upsaclay.message.data.model.MessageField.Remote.MESSAGE_ID
import com.upsaclay.message.data.model.MessageField.Remote.RECIPIENT_ID
import com.upsaclay.message.data.model.MessageField.Remote.SEEN
import com.upsaclay.message.data.model.MessageField.Remote.SENDER_ID
import com.upsaclay.message.data.model.MessageField.Remote.TIMESTAMP
import com.upsaclay.message.data.remote.model.RemoteMessage
import com.upsaclay.message.data.remote.model.RemoteMessageReport
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.entity.MessageReport

internal fun RemoteMessage.toMessage() = Message(
    id = messageId,
    senderId = senderId,
    recipientId = recipientId,
    conversationId = conversationId,
    content = content,
    date = timestamp.toLocalDateTime(),
    seen = seen,
    state = MessageState.SENT
)

internal fun RemoteMessage.toMap(): Map<String, Any> {
    val data = mutableMapOf<String, Any>()
    data[MESSAGE_ID] = messageId
    data[CONVERSATION_ID] = conversationId
    data[SENDER_ID] = senderId
    data[RECIPIENT_ID] = recipientId
    data[CONTENT] = content
    data[TIMESTAMP] = timestamp
    data[SEEN] = seen
    return data
}

fun LocalMessage.toMessage() = Message(
    id = messageId,
    senderId = messageSenderId,
    recipientId = messageRecipientId,
    conversationId = messageConversationId,
    content = messageContent,
    date = messageTimestamp.toLocalDateTimeUTC(),
    seen = messageSeen,
    state = MessageState.valueOf(messageState)
)

fun Message.toLocal() = LocalMessage(
    messageId = id,
    messageSenderId = senderId,
    messageRecipientId = recipientId,
    messageConversationId = conversationId,
    messageContent = content,
    messageTimestamp = date.toEpochMilliUTC(),
    messageSeen = seen,
    messageState = state.name
)

internal fun Message.toRemote() = RemoteMessage(
    messageId = id,
    conversationId = conversationId,
    senderId = senderId,
    recipientId = recipientId,
    content = content,
    timestamp = date.toTimestamp(),
    seen = seen
)

internal fun MessageReport.toRemote() = RemoteMessageReport(
    conversationId = conversationId,
    messageId = messageId,
    recipient = recipient.toRemote(),
    reason = reason.toString()
)

internal fun MessageReport.Recipient.toRemote() = RemoteMessageReport.RemoteRecipient(
    fullName = fullName,
    email = email
)