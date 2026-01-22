package com.upsaclay.message.domain.fixtures

import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationDTO
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.entity.MessageNotification
import java.time.LocalDateTime
import java.time.ZoneOffset

val messageFixture = Message(
    id = "1",
    senderId = userFixture2.id,
    recipientId = userFixture.id,
    conversationId = "1",
    content = "Salut, comment tu vas ?",
    date = LocalDateTime.now(),
    seen = true,
    state = MessageState.SENT
)

val messageFixture2 = Message(
    id = "1",
    senderId = userFixture.id,
    recipientId = userFixture2.id,
    conversationId = "1",
    content = "Bonjour, j'espère que vous allez bien. " +
            "Je voulais prendre un moment pour vous parler de quelque chose d'important. " +
            "En fait, je pense qu'il est essentiel que nous discutions de la direction que prend notre projet, " +
            "car il y a plusieurs points que nous devrions clarifier.",
    date = LocalDateTime.now(),
    seen = true,
    state = MessageState.SENT
)

val messagesFixture = listOf(
    Message(
        id = "1",
        senderId = userFixture.id,
        recipientId = usersFixture[1].id,
        conversationId = conversationFixture.id,
        content = "On s'y retrouve à 14h. 👍",
        date = LocalDateTime.now(),
        seen = true,
        state = MessageState.SENT
    ),
    Message(
        id = "2",
        senderId = usersFixture[1].id,
        recipientId = userFixture.id,
        conversationId = conversationFixture.id,
        content = "Top ! Ca va être super.",
        date = LocalDateTime.now().minusMinutes(1),
        seen = true,
        state = MessageState.SENT
    ),
    Message(
        id = "4",
        senderId = userFixture.id,
        recipientId = usersFixture[1].id,
        conversationId = conversationFixture.id,
        content = "J'ai ramené quelques cousins venu de l'étranger.",
        date = LocalDateTime.now().minusMinutes(3),
        seen = true,
        state = MessageState.SENT
    ),
    Message(
        id = "5",
        senderId = usersFixture[1].id,
        recipientId = userFixture.id,
        conversationId = conversationFixture.id,
        content = "Je suis en route..",
        date = LocalDateTime.now().minusMinutes(4),
        seen = true,
        state = MessageState.SENT
    ),
    Message(
        id = "6",
        senderId = userFixture.id,
        recipientId = usersFixture[1].id,
        conversationId = conversationFixture.id,
        content = "On m'a signalé que l'événement avait commencé.",
        date = LocalDateTime.now().minusMinutes(5),
        seen = true,
        state = MessageState.SENT
    )
)

val messageNotificationFixture = MessageNotification(
    conversation = conversationFixture,
    messageContent = MessageNotification.MessageContent(
        messageId = messageFixture.id,
        content = messageFixture.content,
        timestamp = messageFixture.date.toEpochMilliUTC()
    )
)

val messageContentNotificationsFixture = listOf(
    messageNotificationFixture,
    messageNotificationFixture.copy(
        conversation = conversationFixture.copy(id = "2"),
        messageContent = MessageNotification.MessageContent(
            messageId = "2",
            content = messageFixture.content,
            timestamp = messageFixture.date.toEpochMilliUTC(),
        )
    )
)