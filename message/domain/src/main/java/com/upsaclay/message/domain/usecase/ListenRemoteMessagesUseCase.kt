package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteMessagesUseCase(
    private val messageRepository: MessageRepository,
    private val blockedUserRepository: BlockedUserRepository,
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) {
    internal var listeningJobs = mutableMapOf<String, Job>()
        private set

    fun start(scope: CoroutineScope) {
        conversationRepository.getConversationsFlow()
            .flatMapMerge { it.asFlow() }
            .filterNot { listeningJobs.containsKey(it.id) }
            .onEach { conversation ->
                val job = scope.launch {
                    val user = userRepository.getLocalUser ?: userRepository.user.first()
                    listenRemoteMessages(user.id, conversation)
                }
                listeningJobs[conversation.id] = job
            }
            .onCompletion { listeningJobs.clear() }
            .launchIn(scope)
    }

    internal suspend fun listenRemoteMessages(userId: String, conversation: Conversation) {
        val lastMessage = messageRepository.getLastMessage(conversation.id)
        val offsetTime = getOffsetTime(conversation, lastMessage)

        messageRepository.fetchRemoteMessages(
            conversation.id,
            conversation.interlocutor.id,
            offsetTime
        )
        .filter { it.visible }
        .collect { message ->
            val blockedUsers = blockedUserRepository.currentBlockedUsers ?: blockedUserRepository.getLocalBlockedUsers()
            val hideMessage = blockedUsers[message.senderId]?.let { message.date > it.date } ?: false

            runCatching {
                if (hideMessage) {
                    messageRepository.updateMessageVisibility(message, userId, false)
                } else {
                    messageRepository.upsertLocalMessage(message)
                }
            }
        }
    }

    private fun getOffsetTime(conversation: Conversation, lastMessage: Message?): LocalDateTime? =
        listOfNotNull(conversation.effectiveFrom, lastMessage?.date).maxByOrNull { it }
}