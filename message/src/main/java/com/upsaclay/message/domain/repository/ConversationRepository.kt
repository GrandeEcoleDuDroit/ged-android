package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    val conversations: Flow<List<Conversation>>

    fun getConversation(conversationId: String): Flow<Conversation>

    fun createConversation(conversation: Conversation)

    suspend fun createMessage(conversationId: String, message: Message)
}