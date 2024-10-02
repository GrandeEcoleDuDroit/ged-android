package com.upsaclay.message.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.common.domain.model.User
import com.upsaclay.message.data.local.model.LocalConversation
import com.upsaclay.message.data.model.ConversationDTO
import com.upsaclay.message.data.remote.model.RemoteConversation
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message

internal object ConversationMapper {
    private val gson = Gson()

    fun fromLocal(localConversation: LocalConversation): ConversationDTO {
        val type = object : TypeToken<List<Int>>() {}.type
        return ConversationDTO(
            conversationId = localConversation.conversationId,
            participants = gson.fromJson(localConversation.participants, type)
        )
    }

    fun toLocal(conversationDTO: ConversationDTO) = LocalConversation(
        conversationId = conversationDTO.conversationId,
        participants = gson.toJson(conversationDTO.participants)
    )

    fun toLocal(remoteConversation: RemoteConversation) = LocalConversation(
        conversationId = remoteConversation.conversationId,
        participants = gson.toJson(remoteConversation.participants)
    )

    fun toRemote(conversationDTO: ConversationDTO) = RemoteConversation(
        conversationId = conversationDTO.conversationId,
        participants = conversationDTO.participants
    )

    fun toDomain(
        conversationId: String,
        interlocutor: User,
        messages: List<Message>
    ) = Conversation(
        id = conversationId,
        interlocutor = interlocutor,
        messages = messages
    )
}