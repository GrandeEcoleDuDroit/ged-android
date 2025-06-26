package com.upsaclay.authentication.presentation.forgopassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R as authenticationR
import com.upsaclay.common.R as commonR
import com.upsaclay.authentication.domain.usecase.ForgotPasswordUseCase
import com.upsaclay.common.domain.entity.DuplicateDataException
import com.upsaclay.common.domain.entity.ForbiddenException
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.usecase.VerifyEmailFormatUseCase
import com.upsaclay.common.utils.mapNetworkErrorMessage
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event : SharedFlow<SingleUiEvent> = _event
    private var _uiState : MutableStateFlow<ForgotPasswordUiState> = MutableStateFlow(ForgotPasswordUiState())
    internal var uiState : StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(email = email)
        }
    }

    fun sendMail() {

        if(!validateInputs(_uiState.value.email)) return
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                forgotPasswordUseCase(email = _uiState.value.email)
                _event.emit(SingleUiEvent.Success())
            } catch (exception : Exception){
                _event.emit(SingleUiEvent.Error(mapErrorMessage(exception)))
            }
            finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun resetValues() {
        _uiState.update { it.copy(email = "") }
    }

    private fun validateInputs(email: String): Boolean {
        _uiState.update {
            it.copy(
                emailError = validateEmail(email),
            )
        }

        return with(_uiState.value) {
            emailError == null
        }
    }

    private fun mapErrorMessage(e: Exception): Int {
        return mapNetworkErrorMessage(e) {
            when (it) {
                // TODO : changer l'implémentation pour obtenir les cas d'exception avec leur message ici
                is NotImplementedError -> authenticationR.string.email_already_associated
                else -> commonR.string.unknown_error
            }
        }
    }

    private fun validateEmail(email: String): Int? {
        return when {
            email.isBlank() -> authenticationR.string.mandatory_field
            !VerifyEmailFormatUseCase(email) -> authenticationR.string.incorrect_email_format_error
            else -> null
        }
    }

    internal data class ForgotPasswordUiState(
        var email: String = "",
        var emailError : Int? = null,
        var loading : Boolean = false,
        @StringRes val emailCode : Int? = null
    )
}