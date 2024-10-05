package com.upsaclay.message.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetAllUserUseCase
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ConversationViewModel(getAllConversationsUseCase: GetAllConversationsUseCase, private val getAllUserUseCase: GetAllUserUseCase) :
    ViewModel() {
    private val _users = MutableStateFlow<List<com.upsaclay.common.domain.model.User>>(emptyList())
    val users: Flow<List<com.upsaclay.common.domain.model.User>> = _users
    val conversations: Flow<List<Conversation>> = getAllConversationsUseCase()

    fun fetchAllUsers() {
        viewModelScope.launch {
            _users.value = getAllUserUseCase()
        }
    }
}