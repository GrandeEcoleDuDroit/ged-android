package com.upsaclay.message.data.repository

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.ConversationPreview
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ConversationRepositoryImpl: ConversationRepository {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    private val _conversationsPreview = MutableStateFlow<List<ConversationPreview>>(emptyList())

    override val conversations: Flow<List<Conversation>> = _conversations
    override val conversationsPreview: Flow<List<ConversationPreview>> = _conversationsPreview

    override fun getConversation(conversationId: Int): Flow<Conversation> {
        TODO("Not yet implemented")
    }
}