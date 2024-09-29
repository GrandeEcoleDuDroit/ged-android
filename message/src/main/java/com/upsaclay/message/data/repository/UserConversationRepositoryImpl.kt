package com.upsaclay.message.data.repository

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.repository.UserConversationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserConversationRepositoryImpl(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
): UserConversationRepository {
    private val _conversations = MutableStateFlow<List<Conversation>>(emptyList())
    override val conversations: Flow<List<Conversation>> = _conversations

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.currentUser?.let { currentUser ->
                conversationRepository.refreshConversations(currentUser.id.toString())
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            userRepository.currentUser?.let { currentUser ->
                conversationRepository.conversationsDTO.map { conversationsDTO ->
                    
                }
            }
        }
    }
}