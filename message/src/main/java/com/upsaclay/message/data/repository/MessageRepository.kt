package com.upsaclay.message.data.repository

import com.upsaclay.message.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun listenLastMessage(conversationId: String): Flow<MessageDTO>

    suspend fun getMessages(conversationId: String, limit: Long): List<MessageDTO>
}