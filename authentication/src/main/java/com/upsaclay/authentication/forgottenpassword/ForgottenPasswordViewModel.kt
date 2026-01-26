package com.upsaclay.authentication.forgottenpassword

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgottenPasswordViewModel : ViewModel() {
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
    internal data class ForgottenPasswordUiState(
        val email : String? = "",
        @StringRes val emailError : Int? = null
    )

}