package com.upsaclay.gedoise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.FetchDataUseCase
import com.upsaclay.app.domain.usecase.ListenBlockedUserEventsUseCase
import com.upsaclay.app.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val fetchDataUseCase: FetchDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEventsUseCase: ListenBlockedUserEventsUseCase,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {
    internal var dataListeningJob: Job? = null
        private set

    fun updateDataOnAuthChange() {
        viewModelScope.launch {
            authenticationRepository.authenticated.collectLatest { authenticated ->
                try {
                    if (authenticated) {
                        connectivityObserver.connected.firstOrNull { it }
                        fetchDataUseCase()
                        startDataListening()
                        fcmTokenUseCase.sendUnsentToken()
                    } else {
                        stopDataListening()
                        delay(2000)
                        clearDataUseCase()
                    }
                } catch (e: Exception) {
                    Timber.e("Error updating data on auth change: ${e.message}", e)
                }
            }
        }
    }

    private fun startDataListening() {
        dataListeningJob?.cancel()
        dataListeningJob = viewModelScope.launch {
            launch { listenRemoteUserUseCase.start() }
            launch { listenRemoteConversationsUseCase.start() }
            launch { listenBlockedUserEventsUseCase.start() }
            awaitCancellation()
        }
    }

    private fun stopDataListening() {
        viewModelScope.launch {
            listenRemoteMessagesUseCase.stopAll()
        }
        dataListeningJob?.cancel()
        dataListeningJob = null
    }
}