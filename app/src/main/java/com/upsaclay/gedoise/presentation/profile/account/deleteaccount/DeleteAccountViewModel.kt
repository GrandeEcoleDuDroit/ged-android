package com.upsaclay.gedoise.presentation.profile.account.deleteaccount

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.usecase.DeleteAccountUseCase
import com.upsaclay.authentication.R
import com.upsaclay.authentication.mapAuthException
import com.upsaclay.common.domain.entity.CustomException
import com.upsaclay.common.domain.entity.CustomException.CustomError.CURRENT_USER_NOT_FOUND
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.extension.executeUiBlockingRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class DeleteAccountViewModel(
    private val userRepository: UserRepository,
    private val deleteAccountUseCase: DeleteAccountUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    val uiState: StateFlow<DeleteAccountUiState> = _uiState

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun deleteUserAccount() {
        val password = uiState.value.password
        if (!validateInput(password)) return

        executeRequest {
            val currentUser = userRepository.currentUser ?: throw CustomException(CURRENT_USER_NOT_FOUND)
            deleteAccountUseCase(currentUser, password)
        }
    }

    private fun executeRequest(block: suspend () -> Unit) {
        viewModelScope.executeUiBlockingRequest(
            block = block,
            onLoading = {
                _uiState.update { it.copy(loading = true) }
            },
            onError = { error ->
                _uiState.update {
                    it.copy(errorMessage = mapAuthException(error))
                }
            },
            onFinished = {
                _uiState.update { it.copy(loading = false) }
            }
        )
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

    data class DeleteAccountUiState(
        val password: String = "",
        val loading: Boolean = false,
        @StringRes val passwordError: Int? = null,
        @StringRes val errorMessage: Int? = null
    )
}