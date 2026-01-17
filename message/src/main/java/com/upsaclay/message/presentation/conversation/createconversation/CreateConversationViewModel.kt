package com.upsaclay.message.presentation.conversation.createconversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.usecase.GetConversationUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateConversationViewModel(
    private val userRepository: UserRepository,
    private val blockedUserRepository: BlockedUserRepository,
    private val getConversationUseCase: GetConversationUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateConversationUiState())
    val uiState: StateFlow<CreateConversationUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private var defaultUsers: List<User> = emptyList()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()
            try {
                getUsersUseCase()
                    .filter { it.id != userRepository.currentUser?.id && it.id !in blockedUserIds }
                    .sortedBy { it.fullName }
                    .also { users ->
                        defaultUsers = users
                        _uiState.update {
                            it.copy(users = users)
                        }
                    }
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(e)))
                _uiState.update {
                    it.copy(users = emptyList())
                }
            }
        }
    }

    suspend fun getConversation(interlocutor: User): Conversation? {
        return try {
            getConversationUseCase(interlocutor)
        } catch (e: Exception) {
            _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(e)))
            null
        }
    }

    fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(query = query)
        }

        val users = if (query.isBlank()) {
            defaultUsers
        } else {
            defaultUsers.filter { user ->
                user.fullName.contains(query, ignoreCase = true)
            }
        }

        _uiState.update {
            it.copy(users = users)
        }
    }

    fun resetQuery() {
        _uiState.update {
            it.copy(
                users = defaultUsers,
                query = ""
            )
        }
    }

    data class CreateConversationUiState(
        val users: List<User>? = null,
        val query: String = ""
    )
}