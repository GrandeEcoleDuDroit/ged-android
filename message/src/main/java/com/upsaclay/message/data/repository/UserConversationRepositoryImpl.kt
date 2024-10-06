package com.upsaclay.message.data.repository

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.data.mapper.ConversationMapper
import com.upsaclay.message.data.mapper.MessageMapper
import com.upsaclay.message.data.model.ConversationDTO
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.repository.UserConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserConversationRepositoryImpl(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
): UserConversationRepository {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    override val conversations: Flow<List<Conversation>> = _conversations
    private val currentUser = userRepository.currentUser
    private var previousConversations = emptyList<ConversationDTO>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            currentUser.collect {
                conversationRepository.listenAllConversations(it.id)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            conversationRepository.conversationsDTO.collect { conversationsDTO ->
                updateConversation(conversationsDTO)

                val newsConversationsDTO = conversationsDTO - previousConversations.toSet()

                newsConversationsDTO.forEach { conversationDTO ->
                    launch {
                        messageRepository.listenLastMessages(conversationDTO.conversationId).collect { messagesDTO ->
                            val messages = messagesDTO.map(MessageMapper::toDomain)
                            addNewConversationMessages(conversationDTO.conversationId, messages)
                        }
                    }
                }

                previousConversations = conversationsDTO
            }
        }
    }

    private suspend fun updateConversation(conversationsDTO: List<ConversationDTO>) {
        val currentUserId = currentUser.first().id
        val conversations = conversationsDTO.mapNotNull { conversationDTO ->
            val interlocutorId = conversationDTO.participants.first { it != currentUserId }
            val interlocutor = userRepository.getUser(interlocutorId)
            val messages = messageRepository.getMessages(conversationDTO.conversationId, 10).map(MessageMapper::toDomain)
            interlocutor?.let { ConversationMapper.toDomain(conversationDTO.conversationId, it, messages) }
        }
        _conversations.value = conversations
    }

    private fun addNewConversationMessages(conversationId: String, messages: List<Message>) {
        _conversations.value = _conversations.value.map { conversation ->
            if (conversation.id == conversationId) {
                val updatedMessages = messages.filterNot { conversation.messages.contains(it) } + conversation.messages
                conversation.copy(messages = updatedMessages)
            } else {
                conversation
            }
        }
    }
}