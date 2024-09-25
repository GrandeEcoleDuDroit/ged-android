package com.upsaclay.message.data.repository

import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.repository.UserConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class UserConversationRepositoryImpl(
    private val conversationRepository: ConversationRepository
): UserConversationRepository {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    override val conversations: Flow<List<Conversation>> = _conversations

}