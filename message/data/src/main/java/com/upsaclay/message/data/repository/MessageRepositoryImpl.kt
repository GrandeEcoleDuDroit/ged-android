package com.upsaclay.message.data.repository

import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.mapper.MessageMapper
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.MessageRepository

internal class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
): MessageRepository {
    override suspend fun sendMessage(conversationId: String, message: Message): Result<Unit> {
        val localMessage = MessageMapper.toLocal(conversationId, message)
        messageLocalDataSource.insertMessage(localMessage)

        val remoteMessage = MessageMapper.toRemote(conversationId, message)
        return messageRemoteDataSource.addMessage(conversationId, remoteMessage)
            .onSuccess {
                messageLocalDataSource.updateMessage(localMessage.copy(isSent = true))
            }
    }
}