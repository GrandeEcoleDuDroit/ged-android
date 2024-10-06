package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.Message

interface MessageRepository {
    suspend fun createMessage(conversationId: String, message: Message)
}