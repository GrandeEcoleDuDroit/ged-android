package com.upsaclay.gedoise.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.app.domain.entity.NotificationPreferences
import com.upsaclay.app.domain.repository.PreferencesRepository
import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.FetchDataUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.authentication.domain.entity.AuthenticationState
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.data.utils.e
import com.upsaclay.common.domain.ConnectivityObserver
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val preferencesRepository: PreferencesRepository,
    private val fetchDataUseCase: FetchDataUseCase,
    private val clearDataUseCase: ClearDataUseCase,
    private val fcmTokenUseCase: FcmTokenUseCase,
    private val listenDataUseCase: ListenDataUseCase,
    private val connectivityObserver: ConnectivityObserver
): ViewModel() {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        e(e.message)
    }

    fun startAppDataUpdating() {
        viewModelScope.launch {
            authenticationRepository.authenticationState.collectLatest { state ->
                when (state) {
                    is AuthenticationState.Authenticated -> {
                        connectivityObserver.connected.first { it }
                        runCatching { authenticationRepository.refreshTokenIfNecessary() }
                        runCatching { fetchDataUseCase.execute(state.userId) }
                            .onFailure { Timber.e("Error fetching data: ${it.message}") }
                        listenDataUseCase.start(this, coroutineExceptionHandler)
                        runCatching { fcmTokenUseCase.sendUnsentToken() }
                             .onFailure { Timber.e("Error sending fcm token: ${it.message}") }
                    }

                    is AuthenticationState.Unauthenticated -> {
                        listenDataUseCase.stop()
                        delay(2000)
                        clearDataUseCase.execute()
                    }
                }
            }
        }
    }

    suspend fun getNotificationPreferences(): NotificationPreferences? =
        preferencesRepository.getNotificationPreferences()

    suspend fun storeNotificationPreferences(notificationPreferences: NotificationPreferences) {
        preferencesRepository.storeNotificationPreferences(notificationPreferences)
    }
}