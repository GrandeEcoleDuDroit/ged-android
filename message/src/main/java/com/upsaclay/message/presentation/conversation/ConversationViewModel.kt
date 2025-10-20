package com.upsaclay.message.presentation.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.CurrentUserNotFoundException
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
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
    private val deleteConversationUseCase: DeleteConversationUseCase,
    private val connectivityObserver: ConnectivityObserver
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
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                _uiState.update {
                    it.copy(loading = true)
                }

                val user = userRepository.currentUser ?: throw CurrentUserNotFoundException()
                deleteConversationUseCase(conversation, user.id)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapToErrorMessage(e)))
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

    private fun mapToErrorMessage(e: Throwable): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is CurrentUserNotFoundException -> com.upsaclay.common.R.string.current_user_not_found_error
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class ConversationUiState(
        val conversations: List<ConversationUi>? = null,
        val loading: Boolean = false
    )
}