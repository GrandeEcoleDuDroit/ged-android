package com.upsaclay.news.domain.announcement.usecase

import kotlinx.coroutines.delay

class RefreshAnnouncementsUseCase(private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase) {
    internal var lastRequestTime: Long = 0

    companion object {
        private const val DEBOUNCE_INTERVAL = 10000L
    }

    suspend fun execute() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            fetchAnnouncementsUseCase.execute()
            lastRequestTime = currentTime
        }
    }
}