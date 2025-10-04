package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.e
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListenRemoteConversationsUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val scope: CoroutineScope
) {
    private var job: Job? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun start() {
        job?.cancel()
        job = scope.launch {
            userRepository.user
                .flatMapLatest { user ->
                    conversationRepository.fetchRemoteConversation(user.id)
                        .catch { e("Failed to fetch conversations", it) }
                        .filter { conversation ->
                            conversationRepository.getConversation(conversation.interlocutor.id) != conversation
                        }
                }
                .collect { conversation ->
                    conversationRepository.upsertLocalConversation(conversation)
                    listenRemoteMessagesUseCase.start(conversation)
                }
        }
    }

    fun stop() {
        job?.cancel()
    }
}