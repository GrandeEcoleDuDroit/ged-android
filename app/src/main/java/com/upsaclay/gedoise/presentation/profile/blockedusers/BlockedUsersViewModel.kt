package com.upsaclay.gedoise.presentation.profile.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val blockedUserRepository: BlockedUserRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(BlockedUserUiState())
    val uiState: StateFlow<BlockedUserUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event: SharedFlow<SingleUiEvent?> = _event

    init {
        initBlockedUsers()
    }

    fun unblockUser(userId: String) {
        val currentUserId = userRepository.currentUser?.id ?: return

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(loading = true)
                }
                blockedUserRepository.unblockUser(currentUserId, userId)
                _uiState.update { state ->
                    state.copy(
                        blockedUsers = state.blockedUsers.filterNot { it.id == userId }
                    )
                }
                _event.emit(SingleUiEvent.Success(R.string.unblocked_user))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun initBlockedUsers() {
        viewModelScope.launch {
           val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()
            blockedUserIds.forEach { userId ->
                launch blockedUserUpdate@ {
                    val user = userRepository.getUser(userId) ?: return@blockedUserUpdate
                    _uiState.update { state ->
                        state.copy(blockedUsers = (state.blockedUsers + user).sortedBy { it.fullName })
                    }
                }
            }
        }
    }

    data class BlockedUserUiState(
        val blockedUsers: List<User> = emptyList(),
        val loading: Boolean = false
    )
}