package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListenAuthenticationStateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    private val _authenticated = MutableStateFlow<Boolean?>(null)
    val authenticated: Flow<Boolean> = _authenticated.filterNotNull()
    val isAuthenticated: Boolean
        get() = _authenticated.value == true

    init {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            if (userRepository.getCurrentUser() == null && authenticationRepository.isAuthenticated()) {
                authenticationRepository.logout()
            }

            authenticationRepository.getAuthenticationState().collect { authenticated ->
                _authenticated.update {
                    authenticated
                }
            }
        }
    }
}