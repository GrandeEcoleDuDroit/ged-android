package com.upsaclay.authentication.presentation.forgottenpassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.presentation.SingleUiEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgottenPasswordViewModel : ViewModel() {
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val _uiState = MutableStateFlow(ForgottenPasswordUiState())
    internal val uiState: StateFlow<ForgottenPasswordUiState> = _uiState


    fun onClick ()  {
        TODO("not implemented yet")

    }

    fun onEmailChange (email : String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(email = email)
        }
    }

    fun resetValues() {
        TODO("Not yet implemented")
    }

    internal data class ForgottenPasswordUiState(
        val email : String = "",
        @StringRes val emailError : Int? = null
    )

}