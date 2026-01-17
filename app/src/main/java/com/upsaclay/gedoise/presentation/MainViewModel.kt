package com.upsaclay.gedoise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.app.domain.usecase.SynchronizeDataUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val synchronizeDataUseCase: SynchronizeDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
    private val listenDataUseCase: ListenDataUseCase
): ViewModel() {
    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            authenticationRepository.authenticationState.collectLatest { authenticated ->
                try {
                    if (authenticated) {
                        listenDataUseCase.start()
                        synchronizeDataUseCase()
                        fcmTokenUseCase.sendUnsetToken()
                    } else {
                        listenDataUseCase.stop()
                        delay(2000)
                        clearDataUseCase()
                        fcmTokenUseCase.generateNewToken()
                    }
                } catch (e: Exception) {
                    Timber.e("Error updating data on auth change: ${e.message}", e)
                }
            }
        }
    }
}