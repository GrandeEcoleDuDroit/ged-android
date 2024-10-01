package com.upsaclay.message.data.remote

import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.data.remote.model.RemoteMessage
import kotlinx.coroutines.flow.Flow

class MessageRemoteDataSource(private val messageApi: MessageApi) {
    fun listenLastMessage(conversationId: String): Flow<RemoteMessage> {
        return messageApi.listenLastMessage(conversationId)
    }

    suspend fun getMessages(conversationId: String, limit: Long): List<RemoteMessage> {
        return messageApi.getMessages(conversationId, limit)
    }
}