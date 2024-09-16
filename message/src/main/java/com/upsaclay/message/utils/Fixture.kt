package com.upsaclay.message.utils

import com.upsaclay.common.utils.userFixture
import com.upsaclay.common.utils.userFixture2
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.ConversationItemData
import com.upsaclay.message.domain.model.Message
import java.time.LocalDateTime

val messageFixture = Message(
    id = 1,
    text = "Je te rappelle",
    date = LocalDateTime.of(2024, 7, 20, 10, 0),
    sender = userFixture
)

val messageFixture2 = Message(
    id = 2,
    text = "Salut ça va ? Cela fait longtemps que j'attend de te parler. Pourrait-on se voir ?",
    date = LocalDateTime.now(),
    sender = userFixture2
)

val messagesFixture = listOf(
    messageFixture,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2
)

val conversationFixture = Conversation(
    id = 1,
    interlocutor = userFixture,
    messages = messagesFixture
)

val conversationsFixture = listOf(
    conversationFixture,
    conversationFixture,
    conversationFixture,
    conversationFixture,
    conversationFixture,
    conversationFixture,
    conversationFixture,
    conversationFixture
)

val conversationItemDataFixture = ConversationItemData(
    id = 1,
    interlocutor = userFixture2,
    lastMessage = messageFixture2,
    isRead = true
)

val conversationsItemsDataFixture = listOf(
    conversationItemDataFixture.copy(isRead = false),
    conversationItemDataFixture,
    conversationItemDataFixture,
    conversationItemDataFixture,
    conversationItemDataFixture,
    conversationItemDataFixture,
    conversationItemDataFixture
)