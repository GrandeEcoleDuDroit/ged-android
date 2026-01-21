package com.upsaclay.gedoise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.FetchDataUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val fetchDataUseCase: FetchDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
    private val listenDataUseCase: ListenDataUseCase
): ViewModel() {
    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            authenticationRepository.authenticationState.collectLatest { state ->
                when (state) {
                    is AuthenticationState.Authenticated -> {
                        runCatching { fetchDataUseCase(state.userId) }
                            .onFailure { Timber.e("Error fetching data", it) }
                        listenDataUseCase.start(state.userId)
                         runCatching { fcmTokenUseCase.sendUnsentToken() }
                             .onFailure { Timber.e("Error sending unsent token", it) }
                             .onSuccess { Timber.i("Unsent token sent successfully") }
                    }

                    is AuthenticationState.Unauthenticated -> {
                        listenDataUseCase.stop()
                        delay(2000)
                        clearDataUseCase()
                    }
                }
            }
        }
    }
}