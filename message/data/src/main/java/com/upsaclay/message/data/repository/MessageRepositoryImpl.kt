package com.upsaclay.message.data.repository

import androidx.paging.PagingData
import com.upsaclay.common.data.utils.e
import com.upsaclay.message.data.local.MessageLocalDataSource
import com.upsaclay.message.data.remote.MessageRemoteDataSource
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.LocalDateTime

internal class MessageRepositoryImpl(
    private val messageLocalDataSource: MessageLocalDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource
): MessageRepository {
    override fun getPagingMessages(conversationId: String): Flow<PagingData<Message>> =
        messageLocalDataSource.getMessages(conversationId)

    override fun getNewMessagesFlow(conversationId: String, date: LocalDateTime): Flow<Message?> =
        messageLocalDataSource.getNewMessagesFlow(conversationId, date)

    override suspend fun getLastMessage(conversationId: String): Message? =
        messageLocalDataSource.getLastMessage(conversationId)

    override suspend fun getUnsentMessages(): List<Message> = messageLocalDataSource.getUnsentMessages()

    override fun fetchRemoteMessages(conversationId: String, interlocutorId: String, offsetTime: LocalDateTime?): Flow<Message> =
        messageRemoteDataSource.listenMessages(conversationId, interlocutorId, offsetTime)
            .catch {
                e("Error fetching remote messages for conversation $conversationId", it)
            }

    override suspend fun createLocalMessage(message: Message) {
        messageLocalDataSource.upsertMessage(message)
    }

    override suspend fun createRemoteMessage(message: Message) {
        try {
            messageRemoteDataSource.createMessage(message)
        } catch (e: Exception) {
            e("Error creating remote message ${message.id} for conversation ${message.conversationId}", e)
            throw e
        }
    }

    override suspend fun setMessagesSeen(conversationId: String, currentUserId: String) {
        try {
            messageLocalDataSource.getUserUnseenMessages(conversationId, currentUserId).forEach { message ->
                messageRemoteDataSource.setMessageSeen(message)
            }
            messageLocalDataSource.setMessagesSeen(conversationId, currentUserId)
        } catch (e: Exception) {
            e("Error set messages seen for conversation $conversationId", e)
            throw e
        }
    }

    override suspend fun setMessageSeen(message: Message) {
        try {
            messageRemoteDataSource.setMessageSeen(message)
            messageLocalDataSource.setMessageSeen(message.id)
        } catch (e: Exception) {
            e("Error set message seen ${message.id} for conversation ${message.conversationId}", e)
            throw e
        }
    }

    override suspend fun updateLocalMessage(message: Message) {
        messageLocalDataSource.updateMessage(message)
    }

    override suspend fun upsertLocalMessage(message: Message) {
        messageLocalDataSource.upsertMessage(message)
    }

    override suspend fun deleteLocalMessage(message: Message) {
        messageLocalDataSource.deleteMessage(message)
    }

    override suspend fun deleteLocalMessages(conversationId: String) {
        messageLocalDataSource.deleteMessages(conversationId)
    }

    override suspend fun deleteLocalMessages() {
        messageLocalDataSource.deleteMessages()
    }

    override suspend fun reportMessage(messageReport: MessageReport) {
        try {
            messageRemoteDataSource.reportMessage(messageReport)
        } catch (e: Exception) {
            e("Error reporting message ${messageReport.messageId} for conversation ${messageReport.conversationId}", e)
            throw e
        }
    }
}