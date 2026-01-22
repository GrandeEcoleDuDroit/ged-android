package com.upsaclay.authentication.presentation.authentication

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.authentication.mapAuthException
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val loginUseCase: LoginUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(AuthenticationUiState())
    internal val uiState: StateFlow<AuthenticationUiState> = _uiState

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password)
        }
    }

    fun login() {
        val (email, password) = _uiState.value
        if (!validateInputs(email, password)) return

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(loading = true)
                }
                loginUseCase.execute(email, password)
            } catch (e: Exception)  {
                _uiState.update {
                    it.copy(errorMessage = mapAuthException(e))
                }
                resetPassword()
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun resetValues() {
        _uiState.update {
            it.copy(
                email = "",
                password = "",
                emailError = null,
                passwordError = null,
                errorMessage = null
            )
        }
    }

    private fun resetPassword() {
        _uiState.update {
            it.copy(password = "")
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        _uiState.update {
            it.copy(
                emailError = validateEmail(email),
                passwordError = validatePassword(password),
                errorMessage = null
            )
        }

        return with(_uiState.value) {
            emailError == null && passwordError == null
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.mandatory_field
            !VerifyEmailFormatUseCase.execute(email) -> R.string.incorrect_email_format_error
            else -> null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.mandatory_field
            else -> null
        }
    }

    internal data class AuthenticationUiState(
        val email: String = "",
        val password: String = "",
        val loading: Boolean = false,
        @StringRes val emailError: Int? = null,
        @StringRes val passwordError: Int? = null,
        @StringRes val errorMessage: Int? = null
    )
}