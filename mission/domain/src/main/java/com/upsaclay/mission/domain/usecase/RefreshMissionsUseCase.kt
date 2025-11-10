package com.upsaclay.mission.domain.usecase

import kotlinx.coroutines.delay


class RefreshMissionsUseCase(
    private val synchronizeMissionsUseCase: SynchronizeMissionsUseCase
) {
    internal var lastRequestTime: Long = 0
    companion object {
        private const val DEBOUNCE_INTERVAL = 10000L
    }

    suspend operator fun invoke() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            synchronizeMissionsUseCase()
            lastRequestTime = currentTime
        }
    }
}