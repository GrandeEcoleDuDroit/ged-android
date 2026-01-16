package com.upsaclay.message.data.remote

import com.upsaclay.common.data.exceptions.mapFirebaseException
import com.upsaclay.common.data.exceptions.mapServerException
import com.upsaclay.common.data.extensions.toTimestamp
import com.upsaclay.message.data.mapper.toMessage
import com.upsaclay.message.data.mapper.toRemote
import com.upsaclay.message.data.remote.api.MessageApi
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class MessageRemoteDataSource(
    private val messageApi: MessageApi
) {
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
        withContext(Dispatchers.IO) {
            try {
                messageApi.createMessage(message.toRemote())
            } catch (e: Exception) {
                throw mapFirebaseException(e)
            }
        }
    }

    suspend fun updateSeenMessage(message: Message) {
        withContext(Dispatchers.IO) {
            try {
                messageApi.updateSeenMessage(message.toRemote())
            } catch (e: Exception) {
                throw mapFirebaseException(e)
            }
        }
    }

    suspend fun reportMessage(report: MessageReport) {
        withContext(Dispatchers.IO) {
            try {
                messageApi.reportMessage(report.toRemote())
            } catch (e: Exception) {
                throw mapServerException(e)
            }
        }
    }
}