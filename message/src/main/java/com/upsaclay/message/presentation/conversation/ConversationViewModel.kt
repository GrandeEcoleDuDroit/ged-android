package com.upsaclay.message.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.ExceptionType.CURRENT_USER_NOT_FOUND_EXCEPTION
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapException
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.GetConversationsUiUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val userRepository: UserRepository,
    private val getConversationsUiUseCase: GetConversationsUiUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        listenConversations()
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(loading = true)
                }

                val user = userRepository.currentUser ?: throw CustomException(CURRENT_USER_NOT_FOUND_EXCEPTION, Exception())
                deleteConversationUseCase(conversation, user.id)
                _event.emit(SingleUiEvent.Success(R.string.conversation_deleted))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapException(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun listenConversations() {
        viewModelScope.launch {
            getConversationsUiUseCase().collectLatest { conversations ->
                _uiState.update {
                    it.copy(conversations = conversations)
                }
            }
        }
    }

    data class ConversationUiState(
        val conversations: List<ConversationUi>? = null,
        val loading: Boolean = false
    )
}