package com.upsaclay.common.presentation.user

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.utils.mapNetworkErrorMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState
    private val _event = MutableStateFlow<SingleUiEvent?>(null)
    val event: StateFlow<SingleUiEvent?> = _event

    init {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update {
                    it.copy(user = user)
                }
            }
        }
    }

    fun reportUser(report: UserReport) {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                userRepository.reportUser(report)
                _event.emit(UserUiEvent.UserReported(R.string.user_reported))
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    data class UserUiState(
        val user: User? = null,
        val loading: Boolean = false,
    )

    sealed interface UserUiEvent: SingleUiEvent {
        data class UserReported(@StringRes val messageId: Int): UserUiEvent
    }
}