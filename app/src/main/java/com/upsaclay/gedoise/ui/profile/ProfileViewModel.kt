package com.upsaclay.gedoise.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.usecase.GetUserUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    getUserUseCase: GetUserUseCase,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    val user = getUserUseCase()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}