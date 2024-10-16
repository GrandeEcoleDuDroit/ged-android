package com.upsaclay.gedoise.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.domain.usecase.GetCurrentUserUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authenticationRepository: AuthenticationRepository,
    getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    val user: Flow<User?> = getCurrentUserUseCase()

    fun logout() {
        viewModelScope.launch {
            authenticationRepository.logout()
        }
    }
}