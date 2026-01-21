package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toMessage
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class MessageRemoteDataSource(private val messageApi: MessageApi) {
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO

    fun listenMessages(
        conversationId: String,
        interlocutorId: String,
        offsetTime: LocalDateTime?
    ): Flow<Message> {
        return messageApi.listenMessages(
            conversationId,
            interlocutorId,
            offsetTime?.toTimestamp()
        ).map { it.toMessage() }
    }

    suspend fun createMessage(message: Message) {
        withContext(dispatcher) {
            try {
                messageApi.createMessage(message.toRemote())
            } catch (e: Exception) {
                throw mapFirebaseException(e)
            }
        }
    }

    suspend fun setMessageSeen(message: Message) {
        withContext(dispatcher) {
            try {
                messageApi.setMessageSeen(message.conversationId, message.id)
            } catch (e: Exception) {
                throw mapFirebaseException(e)
            }
        }
    }

    suspend fun updateMessageVisibility(message: Message, userId: String, visible: Boolean) {
        withContext(dispatcher) {
            try {
                messageApi.updateMessageVisibility(message.toRemote(), userId, visible)
            } catch (e: Exception) {
                throw mapFirebaseException(e)
            }
        }
    }

    suspend fun reportMessage(report: MessageReport) {
        withContext(dispatcher) {
            try {
                messageApi.reportMessage(report.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}