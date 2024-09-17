package com.upsaclay.message.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetAllUserUseCase
import com.upsaclay.message.domain.usecase.GetAllConversationsPreviewUseCase
import com.upsaclay.message.utils.conversationsPreviewFixture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ConversationViewModel(
    getAllConversationsPreviewUseCase: GetAllConversationsPreviewUseCase,
    private val getAllUserUseCase: GetAllUserUseCase
): ViewModel() {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: Flow<List<User>> = _users
//    val conversations: Flow<List<ConversationPreview>> = getAllConversationsPreviewUseCase()
    val conversations = conversationsPreviewFixture
    fun fetchAllUsers() {
        viewModelScope.launch {
            _users.value = getAllUserUseCase()
        }
    }
}