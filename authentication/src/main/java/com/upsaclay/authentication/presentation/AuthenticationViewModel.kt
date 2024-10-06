package com.upsaclay.authentication.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.model.AuthenticationState
import com.upsaclay.authentication.domain.usecase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _authenticationState = MutableStateFlow(AuthenticationState.UNAUTHENTICATED)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun login() {
        _authenticationState.value = AuthenticationState.LOADING

        if (!verifyInputs()) {
            _authenticationState.value = AuthenticationState.INPUT_ERROR
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(email, password)
                .onSuccess {
                    _authenticationState.value = AuthenticationState.AUTHENTICATED
                }
                .onFailure {
                    _authenticationState.value = AuthenticationState.AUTHENTICATION_ERROR
                }
        }
    }

    private fun verifyInputs(): Boolean = !(email.isBlank() || password.isBlank())
}