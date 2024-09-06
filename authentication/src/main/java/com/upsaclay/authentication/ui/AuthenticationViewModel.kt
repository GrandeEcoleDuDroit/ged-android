package com.upsaclay.authentication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.model.AuthenticationState
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _authenticationState = MutableStateFlow(AuthenticationState.UNAUTHENTICATED)
    val authenticationState: Flow<AuthenticationState> = _authenticationState
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
            _authenticationState.value = AuthenticationState.INPUT_ERROR
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(mail, password)
                .onSuccess {
                    _authenticationState.value = AuthenticationState.AUTHENTICATED
                }
                .onFailure {
                    _authenticationState.value = AuthenticationState.AUTHENTICATION_ERROR
                }
        }
    }

    private fun verifyInputs(): Boolean = !(mail.isBlank() || password.isBlank())
}