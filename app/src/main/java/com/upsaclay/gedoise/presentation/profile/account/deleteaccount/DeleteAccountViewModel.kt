package com.upsaclay.gedoise.presentation.profile.account.deleteaccount

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.entity.exception.InvalidCredentialsException
import com.upsaclay.authentication.domain.entity.exception.UserDisabledException
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.CurrentUserNotFoundException
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteAccountViewModel(
    private val userRepository: UserRepository,
    private val deleteAccountUseCase: com.upsaclay.app.domain.DeleteAccountUseCase,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {
    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    val uiState: StateFlow<DeleteAccountUiState> = _uiState
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun deleteUserAccount() {
        val password = uiState.value.password
        if (!validateInput(password)) return

        viewModelScope.launch {
            try {
                val currentUser = userRepository.currentUser ?: throw CurrentUserNotFoundException()
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }

                _uiState.update {
                    it.copy(loading = true)
                }
                deleteAccountUseCase(currentUser, password)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(errorMessage = mapErrorMessage(e))
                }
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    private fun validateInput(password: String): Boolean {
        _uiState.update {
            it.copy(
                passwordError = validatePassword(password),
                errorMessage = null
            )
        }

        return with(_uiState.value) {
            passwordError == null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.mandatory_field
            else -> null
        }
    }

    private fun mapErrorMessage(e: Throwable): Int {
        return mapNetworkErrorMessage(e) {
            when (e) {
                is InvalidCredentialsException -> R.string.invalid_credentials_error
                is UserDisabledException -> R.string.user_disabled_error
                is CurrentUserNotFoundException -> com.upsaclay.common.R.string.current_user_not_found_error
                else -> com.upsaclay.common.R.string.unknown_error
            }
        }
    }

    data class DeleteAccountUiState(
        val password: String = "",
        val loading: Boolean = false,
        @StringRes val passwordError: Int? = null,
        @StringRes val errorMessage: Int? = null
    )
}