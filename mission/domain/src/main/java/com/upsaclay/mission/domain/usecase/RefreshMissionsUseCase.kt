package com.upsaclay.mission.domain.usecase

import kotlinx.coroutines.delay

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshMissionsUseCase(
    private val synchronizeMissionsUseCase: SynchronizeMissionsUseCase
) {
    internal var lastRequestTime: Long = 0

    suspend operator fun invoke() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            synchronizeMissionsUseCase()
            lastRequestTime = currentTime
        }
    }
}