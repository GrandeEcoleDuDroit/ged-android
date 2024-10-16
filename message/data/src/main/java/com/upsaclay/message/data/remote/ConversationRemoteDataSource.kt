package com.upsaclay.message.data.remote

import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

internal class ConversationRemoteDataSource(private val conversationApi: ConversationApi) {
    fun listenAllConversations(userId: Int): Flow<List<RemoteConversation>> =
        conversationApi.listenAllConversations(userId)

    suspend fun createConversation(remoteConversation: RemoteConversation): String =
        conversationApi.createConversation(remoteConversation)
}