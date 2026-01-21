package com.upsaclay.gedoise.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.usecase.LogoutUseCase
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    userRepository: UserRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        viewModelScope.launch {
            userRepository.user.collect { user ->
                _uiState.update { it.copy(user = user) }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(loading = true) }
                logoutUseCase()
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    data class ProfileUiState(
        val user: User? = null,
        val loading: Boolean = false
    )
}