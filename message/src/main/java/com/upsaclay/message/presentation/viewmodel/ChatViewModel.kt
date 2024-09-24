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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    val chatState: StateFlow<ChatState> = _chatState
    private val _interlocutor = MutableStateFlow<User?>(null)
    val interlocutor: StateFlow<User?> = _interlocutor
    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation
    val currentUser: User? = getCurrentUserUseCase()
    var text: String by mutableStateOf("")
        private set

    init {
        _chatState.value = ChatState.LOADING
        conversationId?.let {
            viewModelScope.launch {
                getConversationUseCase(conversationId).collect {
                    _conversation.value = it
                    _chatState.value = ChatState.DEFAULT
                }
            }
        }

        interlocutorId?.let {
            viewModelScope.launch {
                getUserUseCase(interlocutorId)?.let {
                    _interlocutor.value = it
                    _chatState.value = ChatState.DEFAULT
                }
            }
        }
    }

    fun updateText(text: String) {
        this.text = text
    }

    fun sendMessage() {
        if(currentUser != null) {
            val messages = _conversation.value?.messages?.toMutableList() ?: mutableListOf()
            val message = Message(
                text = text,
                date = LocalDateTime.now(),
                sender = currentUser
            )
            messages.add(message)
            val conversation = _conversation.value ?:
                Conversation(interlocutor = _interlocutor.value!!, messages = messages)
            _conversation.value = conversation
            viewModelScope.launch {
                sendMessageUseCase(conversation.id, message)
            }
        } else {
            _chatState.value = ChatState.SENT_MESSAGE_ERROR
        }
    }
}