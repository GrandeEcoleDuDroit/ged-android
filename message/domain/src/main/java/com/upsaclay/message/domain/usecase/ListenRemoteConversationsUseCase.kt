package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest

class ListenRemoteConversationsUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun start() {
        userRepository.user
            .flatMapLatest { user ->
                conversationRepository.fetchRemoteConversations(user.id)
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