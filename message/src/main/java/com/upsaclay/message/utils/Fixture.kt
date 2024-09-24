package com.upsaclay.message.utils

import com.upsaclay.common.utils.userFixture
import com.upsaclay.common.utils.userFixture2
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.ConversationPreview
import com.upsaclay.message.domain.model.Message
import java.time.LocalDateTime

val messageFixture = Message(
    id = "1",
    text = "Salut, bien et toi ? Oui bien sûr.",
    date = LocalDateTime.of(2024, 7, 20, 10, 0),
    sender = userFixture,
    isRead = true
)

val messageFixture2 = Message(
    id = "2",
    text = "Salut ça va ? Cela fait longtemps que j'attend de te parler. Pourrait-on se voir ?",
    date = LocalDateTime.now(),
    sender = userFixture2,
    isRead = false
)

val messagesFixture = listOf(
    messageFixture,
    messageFixture.copy(text = "Yes"),
    messageFixture,
    messageFixture2,
    messageFixture2,
    messageFixture2,
    messageFixture2.copy(text = "T'es dispo ?"),
    messageFixture,
    messageFixture2,
    messageFixture,
    messageFixture2,
    messageFixture2,
    messageFixture2
)

val conversationFixture = Conversation(
    id = "1",
    interlocutor = userFixture2,
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