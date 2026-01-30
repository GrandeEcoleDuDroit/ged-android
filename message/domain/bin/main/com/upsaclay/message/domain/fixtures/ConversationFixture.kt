package com.upsaclay.message.domain.fixtures

import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.userFixture3
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationDTO
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationUi
import java.time.LocalDateTime

val conversationFixture = Conversation(
    id = "1",
    interlocutor = usersFixture[1],
    createdAt = LocalDateTime.now().minusYears(1),
    state = Conversation.ConversationState.CREATED,
    effectiveFrom = null
)

val conversationUiFixture = ConversationUi(
    id = "1",
    interlocutor = userFixture2,
    lastMessage = messageFixture,
    createdAt = LocalDateTime.now(),
    state = Conversation.ConversationState.CREATED
)

val conversationDTOFixture = ConversationDTO(
    conversationId = conversationFixture.id,
    participants = listOf(userFixture.id, conversationFixture.interlocutor.id),
    createdAt = conversationFixture.createdAt,
    effectiveFrom = conversationFixture.effectiveFrom
)

val conversationMessageFixture = ConversationMessage(
    conversation = conversationFixture,
    lastMessage = messageFixture
)

val conversationsUiFixture = listOf(
    conversationUiFixture.copy(
        lastMessage = messageFixture.copy(seen = false)
    ),
    conversationUiFixture.copy(
        id = "2",
        interlocutor = usersFixture[2],
        lastMessage = messageFixture.copy(
            senderId = usersFixture[2].id,
            seen = false,
            date = LocalDateTime.now().minusMinutes(5),
            content = "Bonne vacance !"
        )
    ),
    conversationUiFixture.copy(
        id = "3",
        interlocutor = usersFixture[1],
        lastMessage = messageFixture.copy(
            seen = true,
            date = LocalDateTime.now().minusHours(1),
            content = "On s'y retrouve à 14h. 👍"
        )
    ),
    conversationUiFixture.copy(
        id = "4",
        interlocutor = userFixture3,
        lastMessage = messageFixture.copy(
            seen = true,
            date = LocalDateTime.now().minusHours(1),
            content = "Le prof a oublié les devoirs 😂"
        )
    ),
    conversationUiFixture.copy(
        id = "5",
        interlocutor = usersFixture[3],
        lastMessage = messageFixture.copy(
            seen = true,
            date = LocalDateTime.now().minusDays(1),
            content = "Camomille + thé matcha 🍵"
        )
    ),
    conversationUiFixture.copy(
        id = "6",
        interlocutor = usersFixture[5],
        lastMessage = messageFixture.copy(
            seen = true,
            date = LocalDateTime.now().minusMonths(2),
            content = "Prochaine session demain matin à 9h."
        )
    )
)

val conversationsFixture = listOf(
    conversationFixture,
    conversationFixture.copy(id = "2"),
    conversationFixture.copy(id = "3"),
    conversationFixture.copy(id = "4"),
    conversationFixture.copy(id = "5")
)