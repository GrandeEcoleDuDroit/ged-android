package com.upsaclay.message.data.remote.api

import com.upsaclay.message.data.remote.RemoteConversation

internal interface ConversationApi {
    suspend fun getConversation(conversationId: String): RemoteConversation?

    suspend fun getAllConversations(userId: String): List<RemoteConversation>

    fun createConversation(conversation: RemoteConversation)
}