package com.upsaclay.gedoise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.ClearDataUseCase
import com.upsaclay.app.domain.FcmTokenUseCase
import com.upsaclay.app.domain.ListenBlockedUserEvents
import com.upsaclay.app.domain.ListenRemoteUserUseCase
import com.upsaclay.app.domain.SynchronizeDataUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase,
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase,
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase,
    private val listenBlockedUserEvents: ListenBlockedUserEvents,
    private val synchronizeDataUseCase: SynchronizeDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
    private val authenticationRepository: AuthenticationRepository
): ViewModel() {
    private var listeningJob: Job? = null

    fun listenAuthenticationChanges() {
        viewModelScope.launch {
            authenticationRepository.authenticationState.collectLatest { authenticated ->
                try {
                    if (authenticated) {
                        listenData()
                        synchronizeDataUseCase()
                        fcmTokenUseCase.sendUnsetToken()
                    } else {
                        stopListenData()
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

    private fun listenData() {
        listeningJob?.cancel()
        listeningJob = viewModelScope.launch {
            launch { listenRemoteConversationsUseCase.start() }
            launch { listenRemoteUserUseCase.start() }
            launch { listenBlockedUserEvents.start() }
        }
    }

    private suspend fun stopListenData() {
        listenRemoteMessagesUseCase.stopAll()
        listeningJob?.cancel()
    }
}