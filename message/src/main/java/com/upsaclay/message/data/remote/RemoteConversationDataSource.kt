package com.upsaclay.message.data.remote

import com.upsaclay.message.data.remote.api.ConversationApi

internal class RemoteConversationDataSource(
    private val conversationApi: ConversationApi
) {
    suspend fun getConversation(conversationId: String) = conversationApi.getConversation(conversationId)

    suspend fun getAllConversations(userId: String) = conversationApi.getAllConversations(userId)

    fun createConversation(remoteConversation: RemoteConversation) {
        conversationApi.createConversation(remoteConversation)
    }
}