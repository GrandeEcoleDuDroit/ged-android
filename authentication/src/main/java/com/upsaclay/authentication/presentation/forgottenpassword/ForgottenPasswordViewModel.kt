package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.authentication.mapAuthException
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.extension.executeUiBlockingRequest
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapExceptionErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgottenPasswordViewModel(private val repository: AuthenticationRepository) : ViewModel() {
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val _uiState = MutableStateFlow(ForgottenPasswordUiState())
    internal val uiState: StateFlow<ForgottenPasswordUiState> = _uiState


    fun sendResetEmail ()  {
        executeRequest(email = _uiState.value.email, block = {
            val email = _uiState.value.email
            if(!validateInput(email)) return@executeRequest
            repository.forgotPassword(email)
        })

    }
    fun onEmailChange (email : String) {
        viewModelScope.launch {
            _uiState.update { it.copy(email = email) }
        }
    }
    private fun validateInput(email : String) : Boolean {
        _uiState.update { it.copy(emailError = validateEmail(email)) }
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
    private fun executeRequest(block: suspend () -> Unit,email : String) {
        viewModelScope.executeUiBlockingRequest(
            block = block,
            onLoading = {
                _uiState.update {
                    it.copy(loading = true)
                }
            },
            onError = {
                _event.emit(SingleUiEvent.Error(mapExceptionErrorMessage(it)))
            },
            onFinished = {
                _uiState.update { it.copy(loading = false) }
            }
        )

    }

    internal data class ForgottenPasswordUiState(
        val email : String = "",
        val loading : Boolean = false,
        @StringRes val emailError : Int? = null
    )

}