package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ListenRemoteMessagesUseCase(
    private val messageRepository: MessageRepository,
    private val scope: CoroutineScope
) {
    internal var messageJobs = mutableMapOf<String, MessageJob>()

    fun start(conversation: Conversation) {
        if (messageJobs[conversation.id]?.conversation == conversation) {
            return
        }
        messageJobs[conversation.id]?.job?.cancel()
        val job = scope.launch {
            listenRemoteMessages(conversation)
        }
        messageJobs[conversation.id] = MessageJob(conversation, job)
    }

    fun stop() {
        messageJobs.values.forEach { it.job.cancel() }
        messageJobs.clear()
    }

    internal suspend fun listenRemoteMessages(conversation: Conversation) {
        val lastMessage = messageRepository.getLastMessage(conversation.id)
        val offsetTime = getOffsetTime(conversation, lastMessage)
        messageRepository.fetchRemoteMessages(conversation.id, conversation.interlocutor.id, offsetTime)
            .catch { error ->
                e("Failed to fetch remote message with ${conversation.interlocutor.fullName}", error)
            }
            .collect { message ->
                messageRepository.upsertLocalMessage(message)
            }

    }

    private fun getOffsetTime(conversation: Conversation, lastMessage: Message?): LocalDateTime {
        return when {
            conversation.deleteTime != null && lastMessage?.date != null -> {
                if (conversation.deleteTime > lastMessage.date) {
                    conversation.deleteTime
                } else {
                    lastMessage.date
                }
            }
            conversation.deleteTime != null -> conversation.deleteTime

            lastMessage?.date != null -> lastMessage.date

            else -> conversation.createdAt
        }
    }


    internal data class MessageJob(
        val conversation: Conversation,
        val job: Job
    )
}