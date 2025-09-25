package com.upsaclay.common.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    userId: String,
    private val userRepository: UserRepository,
    private val blockedUserRepository: BlockedUserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent?>()
    val event: SharedFlow<SingleUiEvent?> = _event

    init {
        listenCurrentUser()
        listenBlockedUserIds(userId)
    }

    fun reportUser(report: UserReport) {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                userRepository.reportUser(report)
                _event.emit(SingleUiEvent.Success(R.string.reported_user))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun blockUser(userId: String) {
        _uiState.update { it.copy(loading = true) }
        val currentUserId = uiState.value.currentUser?.id ?: return

        viewModelScope.launch {
            try {
                blockedUserRepository.blockUser(currentUserId,userId)
                _event.emit(SingleUiEvent.Success(R.string.blocked_user))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun unblockUser(userId: String) {
        _uiState.update { it.copy(loading = true) }
        val currentUserId = uiState.value.currentUser?.id ?: return

        viewModelScope.launch {
            try {
                blockedUserRepository.unblockUser(currentUserId, userId)
                _event.emit(SingleUiEvent.Success(R.string.unblocked_user))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun listenCurrentUser() {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update {
                    it.copy(currentUser = user)
                }
            }
        }
    }

    private fun listenBlockedUserIds(userId: String) {
        viewModelScope.launch {
            blockedUserRepository.blockedUserIds.collect { blockedUserIds ->
                _uiState.update {
                    it.copy(userBlocked = userId in blockedUserIds)
                }
            }
        }
    }

    data class UserUiState(
        val currentUser: User? = null,
        val loading: Boolean = false,
        val userBlocked: Boolean = false
    )
}