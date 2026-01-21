package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationDTO
import com.upsaclay.message.domain.mapper.toConversation
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteConversationsUseCase(
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository
) {
    internal val fetchedInterlocutors = mutableMapOf<String, User>()
    private val mutex = Mutex()

    fun start(scope: CoroutineScope) {
        userRepository.user
            .flatMapLatest { listenConversations(it) }
            .onEach { conversationRepository.upsertLocalConversation(it) }
            .onCompletion { fetchedInterlocutors.clear() }
            .launchIn(scope)
    }

    private suspend fun listenConversations(user: User): Flow<Conversation> {
        return conversationRepository.getRemoteConversationsFlow(user.id)
            .flatMapMerge { conversationDTO ->
                getInterlocutorFlow(conversationDTO, user.id)
                    .map { conversationDTO.toConversation(it) }
            }
    }

    private suspend fun getInterlocutorFlow(conversationDTO: ConversationDTO, currentUserId: String): Flow<User> {
        val interlocutorId = conversationDTO.participants.firstOrNull { it != currentUserId } ?: return emptyFlow()

        val interlocutor = mutex.withLock {
            fetchedInterlocutors[interlocutorId]
        }

        return interlocutor?.let {
            flowOf(it)
        } ?: run {
            userRepository.getUserFlow(interlocutorId)
                .filterNotNull()
                .onEach {
                    mutex.withLock {
                        fetchedInterlocutors[interlocutorId] = it
                    }
                }
        }
    }
}