package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

interface ConversationApi {
    fun listenAllConversations(userId: Int): Flow<List<RemoteConversation>>

    fun updateConversation(remoteConversation: RemoteConversation)

    suspend fun createConversation(remoteConversation: RemoteConversation): String
}