package com.upsaclay.authentication.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.data.AuthenticationState
import com.upsaclay.authentication.domain.IsAuthenticatedUseCase
import com.upsaclay.authentication.domain.LoginParisSaclayUseCase
import com.upsaclay.authentication.domain.LogoutUseCase
import com.upsaclay.core.data.SharedPreferenceFiles
import com.upsaclay.core.data.SharedPreferencesKeys
import com.upsaclay.core.domain.SharedPreferenceUseCase
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
    private val sharedPreferenceUseCase: SharedPreferenceUseCase by inject(
        SharedPreferenceUseCase::class.java
    )
    private val _authenticationState = MutableStateFlow(AuthenticationState.UNAUTHENTICATED)
    val authenticationState: StateFlow<AuthenticationState> = _authenticationState.asStateFlow()
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    init {
        _authenticationState.value = if (isAuthenticatedUseCase())
            AuthenticationState.AUTHENTICATED
        else AuthenticationState.UNAUTHENTICATED
    }

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
            val result = loginParisSaclayUseCase(username, password)
            if (result.isSuccess) {
                _authenticationState.value = AuthenticationState.AUTHENTICATED
                sharedPreferenceUseCase.storeBoolean(
                    SharedPreferenceFiles.AUTHENTICATION,
                    SharedPreferencesKeys.IS_AUTHENTICATED,
                    true
                )
            } else {
                _authenticationState.value = AuthenticationState.ERROR_AUTHENTICATION
                Log.e(
                    javaClass.simpleName,
                    "Error while trying to login : ${result.exceptionOrNull()}"
                )
            }
        }
    }

    fun logout() {
        logoutUseCase()
        _authenticationState.value = AuthenticationState.UNAUTHENTICATED
    }

    private fun verifyInputs(): Boolean = !(username.isBlank() || password.isBlank())
}