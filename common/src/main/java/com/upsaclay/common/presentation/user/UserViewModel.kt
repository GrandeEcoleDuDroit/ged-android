package com.upsaclay.common.presentation.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
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
        executeRequest {
            userRepository.reportUser(report)
            _event.emit(SingleUiEvent.Success(R.string.reported_user))
        }
    }

    fun blockUser(userId: String) {
        executeRequest {
            val currentUserId = uiState.value.currentUser?.id ?: throw CustomException(CustomException.CustomError.CURRENT_USER_NOT_FOUND)
            blockedUserRepository.blockUser(currentUserId,userId)
            _event.emit(SingleUiEvent.Success(R.string.blocked_user))
        }
    }

    fun unblockUser(userId: String) {
        executeRequest {
            val currentUserId = uiState.value.currentUser?.id ?: throw CustomException(CustomException.CustomError.CURRENT_USER_NOT_FOUND)
            blockedUserRepository.unblockUser(currentUserId, userId)
            _event.emit(SingleUiEvent.Success(R.string.unblocked_user))
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        viewModelScope.executeUiBlockingRequest(
            block = block,
            onLoading = {
                _uiState.update { it.copy(loading = true) }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(it)))
            },
            onFinished = {
                _uiState.update { it.copy(loading = false) }
            }
        )
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