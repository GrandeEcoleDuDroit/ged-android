package com.upsaclay.message.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetAllUserUseCase
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase
import com.upsaclay.message.domain.usecase.CreateConversationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ConversationViewModel(
    getAllConversationsUseCase: GetAllConversationsUseCase,
    private val createConversationUseCase: CreateConversationUseCase,
    private val getAllUserUseCase: GetAllUserUseCase
) : ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: Flow<List<User>> = _users
    val conversations: Flow<List<Conversation>> = getAllConversationsUseCase()

    init {
        viewModelScope.launch {
            getAllUserUseCase().collectLatest {
                _users.value = it
            }
        }
    }

    fun createNewConversation(interlocutor: User) {
        viewModelScope.launch {
            createConversationUseCase(interlocutor)
        }
    }
}