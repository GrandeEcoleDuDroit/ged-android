package com.upsaclay.gedoise.presentation.profile.privacy.blockedusers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BlockedUsersViewModel(
    private val blockedUserRepository: BlockedUserRepository,
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(BlockedUserUiState())
    val uiState: StateFlow<BlockedUserUiState> = _uiState

    init {
        initBlockedUsers()
    }

    fun unblockUser(userId: String) {
        val currentUser = userRepository.currentUser ?: return

        viewModelScope.launch {
            blockedUserRepository.unblockUser(currentUser.id, userId)
            _uiState.update { state ->
                state.copy(
                    blockedUsers = state.blockedUsers.filterNot { it.id == userId }
                )
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
        val blockedUsers: List<User> = emptyList()
    )
}