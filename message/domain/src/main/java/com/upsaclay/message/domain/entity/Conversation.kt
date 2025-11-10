package com.upsaclay.message.domain.entity

import com.upsaclay.common.domain.entity.User
import java.time.LocalDateTime

data class Conversation(
    val id: String,
    val interlocutor: User,
    val createdAt: LocalDateTime,
    val state: ConversationState,
    val deleteTime: LocalDateTime? = null
) {
    val shouldBeCreated: Boolean
        get() = state == ConversationState.DRAFT ||
                state == ConversationState.ERROR ||
                state == ConversationState.DELETING

    enum class ConversationState {
        DRAFT,
        CREATING,
        CREATED,
        DELETING,
        ERROR
    }
}