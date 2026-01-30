package com.upsaclay.message.domain.repository

import androidx.paging.PagingData
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface MessageRepository {
    fun getPagingMessages(conversationId: String): Flow<PagingData<Message>>

    fun getNewMessagesFlow(conversationId: String, date: LocalDateTime): Flow<Message?>

    suspend fun getLastMessage(conversationId: String): Message?

    suspend fun getUnsentMessages(): List<Message>

    fun fetchRemoteMessages(conversationId: String, interlocutorId: String, offsetTime: LocalDateTime?): Flow<Message>

    suspend fun createLocalMessage(message: Message)

    suspend fun createRemoteMessage(message: Message)

    suspend fun updateLocalMessage(message: Message)

    suspend fun setMessagesSeen(conversationId: String, currentUserId: String)

    suspend fun setMessageSeen(message: Message)

    suspend fun updateMessageVisibility(message: Message, currentUserId: String, visible: Boolean)

    suspend fun upsertLocalMessage(message: Message)

    suspend fun deleteLocalMessage(message: Message)

    suspend fun deleteLocalMessages()

    suspend fun deleteLocalMessages(conversationId: String)

    suspend fun reportMessage(messageReport: MessageReport)
}