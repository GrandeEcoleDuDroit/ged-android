package com.upsaclay.message.data.repository

import com.upsaclay.message.data.model.ConversationDTO
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    val conversationsDTO: Flow<List<ConversationDTO>>

    suspend fun listenConversations(userId: String)

    suspend fun updateConversation(conversationDTO: ConversationDTO)

    suspend fun createConversation(conversationDTO: ConversationDTO)
}