package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

class ListenRemoteMessagesUseCase(
    private val messageRepository: MessageRepository,
    private val blockedUserRepository: BlockedUserRepository
) {
    internal var jobs = mutableMapOf<String, Job>()
    private val mutex = Mutex()

    suspend fun start(conversation: Conversation) {
        mutex.withLock {
            jobs[conversation.interlocutor.id]?.job?.cancel()
        }

        val job = CoroutineScope(Dispatchers.IO).launch {
            val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()
            if (conversation.interlocutor.id !in blockedUserIds) {
                listenMessages(conversation)
            }
        }

        mutex.withLock {
            jobs[conversation.interlocutor.id] = job
        }
    }

    suspend fun stop(userId: String) {
        mutex.withLock {
            jobs[userId]?.job?.cancel()
            jobs.remove(userId)
        }
    }

    suspend fun stopAll() {
        mutex.withLock {
            jobs.values.forEach { it.cancel() }
            jobs.clear()
        }
    }

    internal suspend fun listenMessages(conversation: Conversation) {
        val lastMessage = messageRepository.getLastMessage(conversation.id)
        val offsetTime = getOffsetTime(conversation, lastMessage)

        messageRepository.fetchRemoteMessages(
            conversation.id,
            conversation.interlocutor.id,
            offsetTime
        ).collect { message ->
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