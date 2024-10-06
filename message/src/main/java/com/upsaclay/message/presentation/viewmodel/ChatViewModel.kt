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

class ChatViewModel(
    private val getConversationUseCase: GetConversationUseCase,
    getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {
    private val _chatState = MutableStateFlow(ChatState.DEFAULT)
    val chatState: StateFlow<ChatState> = _chatState

    private val _conversation = MutableStateFlow<Conversation?>(null)
    val conversation: StateFlow<Conversation?> = _conversation

    val currentUser: User? = getCurrentUserUseCase().value
    var text: String by mutableStateOf("")
        private set

    fun updateText(text: String) {
        this.text = text
    }

    fun sendMessage(conversationId: String?, message: Message) {
        viewModelScope.launch {
            sendMessageUseCase(conversationId, message)
        }
    }

    fun getConversation(conversationId: String) {
        viewModelScope.launch {
            getConversationUseCase(conversationId).collect {
                _conversation.value = it
            }
        }
    }
}