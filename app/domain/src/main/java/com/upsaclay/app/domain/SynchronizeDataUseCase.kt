package com.upsaclay.app.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.usecase.SynchronizeBlockedUsersUseCase
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take

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
                synchronizeBlockedUsersUseCase()
                synchronizeAnnouncementsUseCase()
            }
    }
}