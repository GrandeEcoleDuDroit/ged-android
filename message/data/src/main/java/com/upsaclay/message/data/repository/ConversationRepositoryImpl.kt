package com.upsaclay.message.data.repository

import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.data.local.ConversationLocalDataSource
import com.upsaclay.message.data.mapper.toConversation
import com.upsaclay.message.data.remote.ConversationRemoteDataSource
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConversationRepositoryImpl(
    private val userRepository: UserRepository,
    private val conversationLocalDataSource: ConversationLocalDataSource,
    private val conversationRemoteDataSource: ConversationRemoteDataSource,
) : ConversationRepository {
    private val fetchedInterlocutors = mutableMapOf<String, User>()
    private val mutex = Mutex()

    override suspend fun getConversations(): List<Conversation> = conversationLocalDataSource.getConversations()

    override fun getConversationFlow(interlocutorId: String): Flow<Conversation> =
        conversationLocalDataSource.getConversationFlow(interlocutorId).filterNotNull()

    override suspend fun getConversation(interlocutorId: String): Conversation? =
        conversationLocalDataSource.getConversation(interlocutorId)

    override suspend fun fetchRemoteConversations(userId: String): Flow<Conversation> {
        return conversationRemoteDataSource.listenConversations(userId)
            .flatMapMerge { remoteConversation ->
                val interlocutorId = remoteConversation.participants.firstOrNull { it != userId }
                    ?: return@flatMapMerge emptyFlow()

                val interlocutor = mutex.withLock {
                    fetchedInterlocutors[interlocutorId]
                }

                interlocutor?.let {
                    flowOf(remoteConversation.toConversation(userId, it))
                } ?: run {
                    userRepository.listenUser(interlocutorId)
                        .filterNotNull()
                        .map { remoteConversation.toConversation(userId, it) }
                        .onEach {
                            mutex.withLock {
                                fetchedInterlocutors[interlocutorId] = it.interlocutor
                            }
                        }
                }
            }
            .catch {
                e("Error fetching remote conversation for user $userId", it)
            }
    }

    override suspend fun createLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun createRemoteConversation(conversation: Conversation, userId: String) {
        try {
            conversationRemoteDataSource.createConversation(conversation, userId)
        } catch (e: Exception) {
            e("Error creating remote conversation ${conversation.id}", e)
            throw e
        }
    }

    override suspend fun updateConversationDeleteTime(conversation: Conversation, currentUserId: String, deleteTime: LocalDateTime) {
        try {
            conversationRemoteDataSource.updateConversationDeleteTime(conversation.id, currentUserId, deleteTime)
            conversationLocalDataSource.updateConversation(conversation)
        } catch (e: Exception) {
            e("Error updating remote conversation ${conversation.id}", e)
            throw e
        }
    }

    override suspend fun updateLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.updateConversation(conversation)
    }

    override suspend fun upsertLocalConversation(conversation: Conversation) {
        conversationLocalDataSource.upsertConversation(conversation)
    }

    override suspend fun deleteConversation(conversationId: String, currentUserId: String, deleteTime: LocalDateTime) {
        try {
            conversationRemoteDataSource.updateConversationDeleteTime(conversationId, currentUserId, deleteTime)
            conversationLocalDataSource.deleteConversation(conversationId)
        } catch (e: Exception) {
            e("Error deleting remote conversation $conversationId", e)
            throw e
        }
    }

    override suspend fun deleteLocalConversations() {
        conversationLocalDataSource.deleteConversations()
    }
}