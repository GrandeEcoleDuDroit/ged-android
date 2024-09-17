package com.upsaclay.message.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetUserUseCase
import com.upsaclay.message.domain.model.Conversation
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import com.upsaclay.message.utils.conversationFixture
import kotlinx.coroutines.flow.Flow

class ChatViewModel(
    getConversationUseCase: GetConversationUseCase,
    getUserUseCase: GetUserUseCase,
    conversationId: Int
): ViewModel() {
//    val conversation: Flow<Conversation> = getConversationUseCase(conversationId)
    val conversation: Conversation = conversationFixture
    val user: Flow<User> = getUserUseCase()
    var text: String by mutableStateOf("")
        private set

    fun updateText(text: String) {
        this.text = text
    }
}