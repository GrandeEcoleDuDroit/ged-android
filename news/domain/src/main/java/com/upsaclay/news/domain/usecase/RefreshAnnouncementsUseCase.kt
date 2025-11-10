package com.upsaclay.news.domain.usecase

import kotlinx.coroutines.delay

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshAnnouncementsUseCase(
    private val synchronizeAnnouncementsUseCase: SynchronizeAnnouncementsUseCase
) {
    internal var lastRequestTime: Long = 0

    suspend operator fun invoke() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            synchronizeAnnouncementsUseCase()
            lastRequestTime = currentTime
        }
    }
}