package com.upsaclay.mission.domain.usecase

import kotlinx.coroutines.delay


class RefreshMissionsUseCase(
    private val fetchMissionsUseCase: FetchMissionsUseCase
) {
    internal var lastRequestTime: Long = 0
    companion object {
        private const val DEBOUNCE_INTERVAL = 10000L
    }

    suspend fun execute() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            fetchMissionsUseCase.execute()
            lastRequestTime = currentTime
        }
    }
}