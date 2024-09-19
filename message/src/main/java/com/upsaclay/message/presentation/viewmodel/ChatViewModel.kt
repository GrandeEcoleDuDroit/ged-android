package com.upsaclay.message.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetCurrentUserUseCase
import com.upsaclay.common.domain.usecase.GetUserUseCase
import com.upsaclay.message.domain.model.ChatState
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.model.Message
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class ChatViewModel(
    conversationId: String?,
    interlocutorId: Int?,
    getConversationUseCase: GetConversationUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    getUserUseCase: GetUserUseCase,
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {
    private val _chatState = MutableStateFlow(ChatState.DEFAULT)
    val chatState: Flow<ChatState> = _chatState
    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: Flow<Conversation?> = _conversation
    val currentUser: User? = getCurrentUserUseCase()
    lateinit var interlocutor: User
    var text: String by mutableStateOf("")
        private set

    init {
        conversationId?.let {
            viewModelScope.launch {
                getConversationUseCase(conversationId).collect {
                    _conversation.value = it
                }
            }
        }

        interlocutorId?.let {
            viewModelScope.launch {
                getUserUseCase(interlocutorId)?.let {
                    interlocutor = it
                }
            }
        }
    }

    fun updateText(text: String) {
        this.text = text
    }

    fun sendMessage() {
        _conversation.value?.let { conversation ->
            currentUser?.let {
                val message = Message(
                    text = text,
                    date = LocalDateTime.now(),
                    sender = currentUser
                )
                viewModelScope.launch {
                    sendMessageUseCase(conversation.id, message)
                        .onSuccess {
                            _chatState.value = ChatState.MESSAGE_SENT
                        }
                        .onFailure {
                            _chatState.value = ChatState.SENT_MESSAGE_ERROR
                        }
                }
            } ?: {
                _chatState.value = ChatState.SENT_MESSAGE_ERROR
            }
        } ?: {
            _chatState.value = ChatState.SENT_MESSAGE_ERROR
        }
    }
}