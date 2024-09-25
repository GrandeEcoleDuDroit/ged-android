package com.upsaclay.message.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upsaclay.message.data.local.LocalConversation
import com.upsaclay.message.data.model.ConversationDTO
import com.upsaclay.message.data.remote.model.RemoteConversation

object ConversationMapper {
    private val gson = Gson()

    fun fromLocal(localConversation: LocalConversation): ConversationDTO {
        val type = object : TypeToken<List<String>>() {}.type
        return ConversationDTO(
            conversationId = localConversation.conversationId,
            participants = gson.fromJson(localConversation.participants, type)
        )
    }

    fun toLocal(conversationDTO: ConversationDTO) = LocalConversation(
        conversationId = conversationDTO.conversationId,
        participants = gson.toJson(conversationDTO.participants)
    )

    fun toLocal(remoteConversation: RemoteConversation): LocalConversation {
        return LocalConversation(
            conversationId = remoteConversation.conversationId,
            participants = gson.toJson(remoteConversation.participants)
        )
    }

    fun toRemote(conversationDTO: ConversationDTO): RemoteConversation {
        return RemoteConversation(
            conversationId = conversationDTO.conversationId,
            participants = conversationDTO.participants
        )
    }
}