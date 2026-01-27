package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.usecase.ForgottenPasswordUseCase
import com.upsaclay.authentication.mapAuthException
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgottenPasswordViewModel(private val forgotPasswordUseCase: ForgottenPasswordUseCase) : ViewModel() {
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val _uiState = MutableStateFlow(ForgottenPasswordUiState())
    internal val uiState: StateFlow<ForgottenPasswordUiState> = _uiState


    fun onClick ()  {
        val email = _uiState.value.email
        if(!validateInput(email)) return

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(loading = true)
                }
                forgotPasswordUseCase.execute(email)
            } catch (e: Exception)  {
                _uiState.update {
                    it.copy(emailError = mapAuthException(e))
                }
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }
    private fun validateInput(email : String) : Boolean {
        _uiState.value = _uiState.value.copy(
            emailError = validateEmail(email)
        )
        return with(_uiState.value) {
            emailError == null
        }

    }
    private fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> R.string.mandatory_field
            !VerifyEmailFormatUseCase.execute(email) -> R.string.incorrect_email_format_error
            else -> null
        }
    }

    fun onEmailChange (email : String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(email = email)
        }
    }

    fun resetValues() {
        viewModelScope.launch {
            _uiState.value = ForgottenPasswordUiState()
        }
    }

    internal data class ForgottenPasswordUiState(
        val email : String = "",
        val loading : Boolean = false,
        @StringRes val emailError : Int? = null
    )

}