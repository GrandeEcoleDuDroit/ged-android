package com.upsaclay.authentication.domain.usecase

import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update

class ListenAuthenticationStateUseCase(
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) {
    private val _authenticated = MutableStateFlow<Boolean?>(null)
    val authenticated: Flow<Boolean> = _authenticated.filterNotNull()
    val isAuthenticated: Boolean
        get() = _authenticated.value == true

    suspend fun listen() {
        val currentUser = userRepository.getCurrentUser()
        val isAuthenticated = authenticationRepository.isAuthenticated()
        if (currentUser == null && isAuthenticated) {
            authenticationRepository.logout()
        }

        authenticationRepository.getAuthenticationState().collect { authenticated ->
            _authenticated.update {
                authenticated
            }
        }
    }
}