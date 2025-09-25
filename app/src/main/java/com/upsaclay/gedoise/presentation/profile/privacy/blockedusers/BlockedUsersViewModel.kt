package com.upsaclay.gedoise.presentation.profile.privacy.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
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

        _uiState.update {
            it.copy(loading = true)
        }

        viewModelScope.launch {
            try {
                blockedUserRepository.unblockUser(currentUserId, userId)
                _uiState.update { state ->
                    state.copy(
                        blockedUsers = state.blockedUsers.filterNot { it.id == userId }
                    )
                }
                _event.emit(SingleUiEvent.Success(com.upsaclay.common.R.string.unblocked_user))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun initBlockedUsers() {
        viewModelScope.launch {
           val blockedUserIds = blockedUserRepository.getLocalBlockedUserIds()
            blockedUserIds.forEach { userId ->
                launch {
                    val user = userRepository.getUser(userId) ?: return@launch
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