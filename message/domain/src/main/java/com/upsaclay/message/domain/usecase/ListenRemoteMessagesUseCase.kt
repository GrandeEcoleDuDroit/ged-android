package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

class ListenRemoteMessagesUseCase(
    private val messageRepository: MessageRepository,
    private val blockedUserRepository: BlockedUserRepository,
    private val scope: CoroutineScope
) {
    internal var jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    fun start(conversation: Conversation) {
        jobs[conversation.interlocutor.id]?.job?.cancel()

        scope.launch {
            val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()
            if (conversation.interlocutor.id !in blockedUserIds) {
                mutex.withLock {
                    jobs[conversation.interlocutor.id] = scope.launch {
                        storeRemoteMessages(conversation)
                    }
                }
            }
        }
    }

    fun stop(userId: String) {
        scope.launch {
            mutex.withLock {
                jobs[userId]?.job?.cancel()
                jobs.remove(userId)
            }
        }
    }

    fun stopAll() {
        scope.launch {
            mutex.withLock {
                jobs.values.forEach { it.cancel() }
                jobs.clear()
            }
        }
    }

    internal suspend fun storeRemoteMessages(conversation: Conversation) {
        val lastMessage = messageRepository.getLastMessage(conversation.id)
        val offsetTime = getOffsetTime(conversation, lastMessage)

        messageRepository.fetchRemoteMessages(
            conversation.id,
            conversation.interlocutor.id,
            offsetTime
        ).catch { error ->
            e("Failed to fetch remote message with ${conversation.interlocutor.fullName}", error)
        }.collect { message ->
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
}