package com.upsaclay.news.domain.usecase

import kotlinx.coroutines.delay

private const val DEBOUNCE_INTERVAL = 10000L

class RefreshAnnouncementsUseCase(
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase
) {
    internal var lastRequestTime: Long = 0

    suspend fun execute() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            fetchAnnouncementsUseCase.execute()
            lastRequestTime = currentTime
        }
    }
}