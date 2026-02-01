package com.upsaclay.authentication.presentation.registration.thirdregistration

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.usecase.RegisterUseCase
import com.upsaclay.authentication.mapAuthException
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.extension.executeUiBlockingRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private const val MIN_PASSWORD_LENGTH = 8

class ThirdRegistrationViewModel(
    private val registerUseCase: RegisterUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ThirdRegistrationUiState())
    internal val uiState: StateFlow<ThirdRegistrationUiState> = _uiState

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

    fun onLegalNoticeCheckedChange(checked: Boolean) {
        _uiState.update {
            it.copy(legalNoticeChecked = checked)
        }
    }

    fun register(firstName: String, lastName: String, schoolLevel: SchoolLevel) {
        val email = uiState.value.email.trim()
        val password = uiState.value.password
        val legalNoticeChecked = uiState.value.legalNoticeChecked

        if (!validateInputs(email, password)) return

        if (!legalNoticeChecked) {
            _uiState.update {
                it.copy(errorMessage = R.string.legal_notice_error)
            }
            return
        }

        executeRequest {
            registerUseCase.execute(email, password, firstName, lastName, schoolLevel)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        _uiState.update {
            it.copy(
                emailError = validateEmail(email),
                passwordError = validatePassword(password)
            )
        }

        return with(_uiState.value) {
            emailError == null && passwordError == null
        }
    }

    private fun validatePassword(password: String): Int? {
        return when {
            password.isBlank() -> R.string.mandatory_field
            password.length < MIN_PASSWORD_LENGTH -> R.string.password_length_error
            else -> null
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.mandatory_field
            !VerifyEmailFormatUseCase.execute(email) -> R.string.incorrect_email_format_error
            else -> null
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

    internal data class ThirdRegistrationUiState(
        val email: String = "",
        val password: String = "",
        val loading: Boolean = false,
        val legalNoticeChecked: Boolean = false,
        @StringRes val emailError: Int? = null,
        @StringRes val passwordError: Int? = null,
        @StringRes val errorMessage: Int? = null
    )
}