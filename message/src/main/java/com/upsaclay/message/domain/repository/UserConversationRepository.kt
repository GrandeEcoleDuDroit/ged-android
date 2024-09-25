package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.Conversation
import kotlinx.coroutines.flow.Flow

interface UserConversationRepository {
    val conversations: Flow<List<Conversation>>
}