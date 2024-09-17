package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.ConversationPreview
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    val conversations: Flow<List<Conversation>>
    val conversationsPreview: Flow<List<ConversationPreview>>

    fun getConversation(conversationId: Int): Flow<Conversation>
}