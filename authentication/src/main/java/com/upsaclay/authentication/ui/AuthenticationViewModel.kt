package com.upsaclay.authentication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginParisSaclayUseCase
import com.upsaclay.authentication.domain.LogoutUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class AuthenticationViewModel : ViewModel() {
    private val isAuthenticatedUseCase: IsAuthenticatedUseCase by inject(
        IsAuthenticatedUseCase::class.java
    )
    private val loginParisSaclayUseCase: LoginParisSaclayUseCase by inject(
        LoginParisSaclayUseCase::class.java
    )
    private val logoutUseCase: LogoutUseCase by inject(LogoutUseCase::class.java)
    private val _authenticationState = MutableStateFlow(isAuthenticatedUseCase())
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState.asStateFlow()
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun updateUsername(value: String) {
        username = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun loginWithParisSaclay() {
        _authenticationState.value = AuthenticationState.LOADING
        if (!verifyInputs()) {
            _authenticationState.value = AuthenticationState.ERROR_INPUT
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _authenticationState.value = loginParisSaclayUseCase(username, password)
        }
    }

    fun logout() {
        logoutUseCase()
        _authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    private fun verifyInputs(): Boolean = !(username.isEmpty() || password.isEmpty())
}