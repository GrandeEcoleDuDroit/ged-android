package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.mapper.ConversationMapper
import com.upsaclay.message.data.model.ConversationDTO
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

internal class ConversationRepositoryImpl(
    private val conversationLocalDataSource: ConversationLocalDataSource,
    private val conversationRemoteDataSource: ConversationRemoteDataSource
): ConversationRepository {
    private val _conversationsDTO = MutableStateFlow<List<ConversationDTO>>(emptyList())
    override val conversationsDTO: Flow<List<ConversationDTO>> = _conversationsDTO

    init {
        CoroutineScope(Dispatchers.IO).launch {
            conversationLocalDataSource.getAllConversationsFlow().collect { localConversations ->
                _conversationsDTO.value = localConversations.map { ConversationMapper.fromLocal(it) }
            }
        }
    }

    override suspend fun listenAllConversations(userId: Int) {
        conversationRemoteDataSource.listenAllConversations(userId).collect { remoteConversations ->
            remoteConversations.forEach {
                conversationLocalDataSource.upsertConversation(ConversationMapper.toLocal(it))
            }
        }
    }

    override suspend fun updateConversation(conversationDTO: ConversationDTO) {
        conversationRemoteDataSource.updateConversation(ConversationMapper.toRemote(conversationDTO))
        conversationLocalDataSource.upsertConversation(ConversationMapper.toLocal(conversationDTO))
    }

    override suspend fun createConversation(conversationDTO: ConversationDTO) {
        conversationRemoteDataSource.createConversation(ConversationMapper.toRemote(conversationDTO))
        conversationLocalDataSource.upsertConversation(ConversationMapper.toLocal(conversationDTO))
    }
}