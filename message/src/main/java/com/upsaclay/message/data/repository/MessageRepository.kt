package com.upsaclay.message.data.repository

import com.upsaclay.message.data.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun listenLastMessages(conversationId: String): Flow<List<MessageDTO>>

    suspend fun getMessages(conversationId: String, start: Int): List<MessageDTO>
}