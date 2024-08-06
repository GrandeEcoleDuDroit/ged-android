package com.upsaclay.authentication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.model.AuthenticationState
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import com.upsaclay.common.utils.errorLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _authenticationState = MutableStateFlow(AuthenticationState.UNAUTHENTICATED)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState.asStateFlow()
    var mail by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun updateMailText(value: String) {
        mail = value
    }

    fun updatePasswordText(value: String) {
        password = value
    }

    fun login() {
        _authenticationState.value = AuthenticationState.LOADING
        if (!verifyInputs()) {
            _authenticationState.value = AuthenticationState.ERROR_INPUT
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val result = loginUseCase(mail, password)
            if (result.isSuccess) {
                _authenticationState.value =
                    result.getOrDefault(AuthenticationState.ERROR_AUTHENTICATION)
            } else {
                _authenticationState.value = AuthenticationState.ERROR_AUTHENTICATION
                errorLog("", result.exceptionOrNull())
            }
        }
    }

    private fun verifyInputs(): Boolean = !(mail.isBlank() || password.isBlank())
}