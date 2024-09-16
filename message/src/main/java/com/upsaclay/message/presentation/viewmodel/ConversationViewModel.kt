package com.upsaclay.message.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.upsaclay.message.domain.usecase.GetAllConversationsUseCase

class ConversationViewModel(
    getAllConversationsUseCase: GetAllConversationsUseCase
): ViewModel() {
    val conversations = getAllConversationsUseCase()
}