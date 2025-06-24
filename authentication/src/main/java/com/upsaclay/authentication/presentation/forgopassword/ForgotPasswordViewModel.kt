package com.upsaclay.authentication.presentation.forgopassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.upsaclay.authentication.domain.usecase.ForgotPasswordUseCase
import com.upsaclay.common.domain.entity.SingleUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {

    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event : SharedFlow<SingleUiEvent> = _event
    private var _uiState : MutableStateFlow<ForgotPasswordUiState> = MutableStateFlow(ForgotPasswordUiState())
    internal var uiState : StateFlow<ForgotPasswordUiState> = _uiState

    fun onEmailChange(email: String) {
        TODO("mise à jour du texte de mail")

    }

    fun sendMail() {
        TODO("envoyer le mail")
    }

    fun resetValues() {
        TODO("vider le champ")
    }

    internal data class ForgotPasswordUiState(
        var email: String = "",
        @StringRes val emailCode : Int? = null
    )
}