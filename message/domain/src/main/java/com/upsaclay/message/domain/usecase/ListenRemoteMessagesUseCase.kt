package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ListenRemoteMessagesUseCase(
    private val messageRepository: MessageRepository,
    private val blockedUserRepository: BlockedUserRepository,
    private val scope: CoroutineScope
) {
    internal var listeningJobs = mutableMapOf<String, Job>()

    init {
        listenBlockUserEvents()
    }

    fun start(conversation: Conversation) {
        listeningJobs[conversation.interlocutor.id]?.job?.cancel()
        updateListeningJobs(conversation)
    }

    fun stop() {
        listeningJobs.values.forEach { it.job.cancel() }
        listeningJobs.clear()
    }

    private fun updateListeningJobs(conversation: Conversation) {
        scope.launch {
            val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()

            if (conversation.interlocutor.id !in blockedUserIds) {
                listeningJobs[conversation.interlocutor.id] = scope.launch {
                    listenRemoteMessages(conversation)
                }
            }
        }
    }

    internal suspend fun listenRemoteMessages(conversation: Conversation) {
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

    internal fun listenBlockUserEvents() {
        scope.launch {
            blockedUserRepository.blockUserEvent.collect { event ->
                if (event is BlockUserEvent.Block) {
                    listeningJobs[event.userId]?.job?.cancel()
                    listeningJobs.remove(event.userId)
                }
            }
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