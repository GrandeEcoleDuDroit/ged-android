package com.upsaclay.gedoise.domain.usecase

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.usecase.SynchronizeBlockedUsersUseCase
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import timber.log.Timber

class SynchronizeDataUseCase(
    private val synchronizeBlockedUsersUseCase: SynchronizeBlockedUsersUseCase,
    private val synchronizeAnnouncementsUseCase: SynchronizeAnnouncementsUseCase,
    private val connectivityObserver: ConnectivityObserver
) {
    suspend operator fun invoke() {
        connectivityObserver.connected
            .filter { it }
            .take(1)
            .collect {
                try {
                    synchronizeBlockedUsersUseCase()
                    synchronizeAnnouncementsUseCase()
                } catch (e: Exception) {
                    Timber.e(e, "Failed to synchronize data" )
                }
            }
    }
}