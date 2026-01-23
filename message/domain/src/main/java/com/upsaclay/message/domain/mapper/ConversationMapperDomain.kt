package com.upsaclay.message.domain.mapper

import com.upsaclay.common.domain.entity.User
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Conversation.ConversationState
import com.upsaclay.message.domain.entity.ConversationDTO
import com.upsaclay.message.domain.entity.ConversationMessage
import com.upsaclay.message.domain.entity.ConversationUi

fun ConversationUi.toConversation() = Conversation(
    id = id,
    interlocutor = interlocutor,
    createdAt = createdAt,
    state = state
)

fun ConversationMessage.toConversationUI() = ConversationUi(
    id = conversation.id,
    interlocutor = conversation.interlocutor,
    lastMessage = lastMessage,
    createdAt = conversation.createdAt,
    state = conversation.state
)

fun ConversationDTO.toConversation(interlocutor: User) = Conversation(
    id = conversationId,
    interlocutor = interlocutor,
    state = ConversationState.CREATED,
    createdAt = createdAt,
    effectiveFrom = effectiveFrom
)