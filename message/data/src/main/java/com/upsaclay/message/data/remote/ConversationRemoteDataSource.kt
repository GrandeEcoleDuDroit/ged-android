package com.upsaclay.message.data.remote

import com.upsaclay.message.data.remote.api.ConversationApi
import com.upsaclay.message.data.remote.model.RemoteConversation
import kotlinx.coroutines.flow.Flow

internal class ConversationRemoteDataSource(
    private val conversationApi: ConversationApi
) {
    fun listenAllConversations(userId: Int): Flow<List<RemoteConversation>> {
        return conversationApi.listenAllConversations(userId)
    }

    fun updateConversation(remoteConversation: RemoteConversation) {
        conversationApi.updateConversation(remoteConversation)
    }

    suspend fun createConversation(remoteConversation: RemoteConversation): String {
        return conversationApi.createConversation(remoteConversation)
    }
}