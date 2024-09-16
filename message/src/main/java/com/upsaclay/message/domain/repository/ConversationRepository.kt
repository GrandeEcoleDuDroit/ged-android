package com.upsaclay.message.domain.repository

import com.upsaclay.message.domain.model.ConversationItemData
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    val conversations: Flow<List<ConversationItemData>>
}