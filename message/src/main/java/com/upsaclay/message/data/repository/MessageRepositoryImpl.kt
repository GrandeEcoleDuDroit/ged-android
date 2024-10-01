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
        return messageRemoteDataSource.listenLastMessages(conversationId).map { remoteMessages ->
            remoteMessages.map(MessageMapper::toDTO)
        }
    }

    override suspend fun getMessages(conversationId: String, limit: Long): List<MessageDTO> {
        return messageRemoteDataSource
            .getMessages(conversationId, limit)
            .map(MessageMapper::toDTO)
    }
}