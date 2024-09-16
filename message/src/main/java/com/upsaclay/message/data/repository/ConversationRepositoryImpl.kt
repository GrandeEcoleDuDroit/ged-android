package com.upsaclay.message.data.repository

import com.upsaclay.message.domain.model.ConversationItemData
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ConversationRepositoryImpl: ConversationRepository {
    private val _conversations = MutableStateFlow<List<ConversationItemData>>(emptyList())
    override val conversations: Flow<List<ConversationItemData>> = _conversations
}