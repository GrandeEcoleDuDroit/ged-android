package com.upsaclay.gedoise.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.usecase.GetCurrentUserFlowUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    getCurrentUserFlowUseCase: GetCurrentUserFlowUseCase,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    val user = getCurrentUserFlowUseCase()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}