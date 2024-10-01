package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.flow.Flow

interface MessageApi {
    fun listenLastMessage(conversationId: String): Flow<RemoteMessage>

    suspend fun getMessages(conversationId: String, limit: Long): List<RemoteMessage>
}