package com.upsaclay.authentication.presentation.forgopassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.upsaclay.authentication.domain.usecase.ForgotPasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {

    private var _uiState : MutableStateFlow<ForgotPasswordUiState> = MutableStateFlow(ForgotPasswordUiState())
    internal var uiState : StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChange(email: String) {

    }

    fun sendMail() {
        TODO("Not yet implemented")
    }

    internal data class ForgotPasswordUiState(
        var email: String = "",
        @StringRes val emailCode : Int? = null
    )
}