package com.upsaclay.authentication.presentation.forgopassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.R as authenticationR
import com.upsaclay.common.R as commonR
import com.upsaclay.authentication.domain.usecase.ForgotPasswordUseCase
import com.upsaclay.common.domain.entity.NoInternetConnectionException
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
            } catch (noInternetConnexion : NoInternetConnectionException){
                _event.emit(SingleUiEvent.Error(mapErrorMessage(noInternetConnexion)))
            }
            catch (_ : Exception) {
                _uiState.update { it.copy(emailCode = validateEmail(email = _uiState.value.email)) }
            }
            finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun resetValues() {
        _uiState.update { it.copy(email = "",loading = false) }
    }

    private fun validateInputs(email: String): Boolean {
        _uiState.update {
            it.copy(
                emailCode = validateEmail(email),
            )
        }

        return with(_uiState.value) {
            emailCode == null
        }
    }

    private fun mapErrorMessage(e: Exception): Int {
        return mapNetworkErrorMessage(e) {
            when (it) {
                is NoInternetConnectionException -> commonR.string.no_internet_connection
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
        var loading : Boolean = false,
        @StringRes val emailCode : Int? = null
    )
}