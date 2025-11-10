package com.upsaclay.message.domain.converter

import com.google.gson.GsonBuilder
import com.upsaclay.common.domain.adapter.LocalDateTimeAdapter
import com.upsaclay.message.domain.entity.Conversation
import java.time.LocalDateTime

object ConversationJsonParser {
    fun toConversation(conversationJson: String): Conversation? {
        return runCatching {
            GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
                .create()
                .fromJson(conversationJson, Conversation::class.java)
        }.getOrNull()
    }

    fun toJson(conversation: Conversation): String {
        return GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter)
            .create()
            .toJson(conversation)
    }
}