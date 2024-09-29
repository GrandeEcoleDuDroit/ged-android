package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.mapper.MessageMapper
import com.upsaclay.message.data.model.MessageDTO
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MessageRepositoryImpl(
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val messageLocalDataSource: MessageLocalDataSource
): MessageRepository {
    override fun listenLastMessages(conversationId: String): Flow<List<MessageDTO>> {
        return messageRemoteDataSource.listenLastMessages(conversationId)
            .map { messages ->
                messages.map { MessageMapper.toDTO(it) }
            }
    }

    override suspend fun getMessages(conversationId: String, start: Int): List<MessageDTO> {
        return messageRemoteDataSource.getMessages(conversationId, start)
            .map { MessageMapper.toDTO(it) }
    }
}