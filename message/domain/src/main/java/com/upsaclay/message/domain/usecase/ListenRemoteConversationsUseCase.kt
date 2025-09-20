package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.entity.BlockUserEvent
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class ListenRemoteConversationsUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val blockedUserRepository: BlockedUserRepository,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    init {
        listenBlockUserEvents()
    }

    fun start() {
        job?.cancel()
        job = scope.launch {
            userRepository.user
                .collectLatest { user ->
                    conversationRepository.fetchRemoteConversations(user.id)
                        .catch { e("Failed to fetch conversations", it) }
                        .filter { conversation ->
                            conversationRepository.getConversation(conversation.interlocutor.id) != conversation
                        }
                        .collect { conversation ->
                            conversationRepository.upsertLocalConversation(conversation)
                            listenRemoteMessagesUseCase.start(conversation)
                        }
                }
        }
    }

    private fun listenBlockUserEvents() {
        scope.launch {
            blockedUserRepository.blockUserEvent.collect { event ->
                if (event is BlockUserEvent.Unblock) {
                    val currentUser = userRepository.currentUser ?: return@collect
                    val conversation = conversationRepository.getConversation(event.userId)?.also {
                        it.copy(deleteTime = event.date)
                    } ?: return@collect

                    conversationRepository.updateConversationDeleteTime(
                        conversation,
                        currentUser.id,
                        event.date
                    )
                }
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}