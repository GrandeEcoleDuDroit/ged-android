package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.LocalConversationDataSource
import com.upsaclay.message.data.remote.RemoteConversationDataSource
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

internal class ConversationRepositoryImpl(
    private val localConversationDataSource: LocalConversationDataSource,
    private val remoteConversationDataSource: RemoteConversationDataSource
): ConversationRepository {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    override val conversations: Flow<List<Conversation>> = _conversations

    init {

    }
    override fun getConversation(conversationId: String): Flow<Conversation> {

    }

    override fun createConversation(conversation: Conversation) {

    }

    override suspend fun createMessage(conversationId: String, message: Message) {

    }
}