package com.upsaclay.news.domain.post.usecase

import kotlinx.coroutines.delay

class RefreshPostsUseCase(private val fetchPostsUseCase: FetchPostsUseCase) {
    internal var lastRequestTime: Long = 0

    companion object {
        private const val DEBOUNCE_INTERVAL = 10000L
    }

    suspend fun execute() {
        delay(500)
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRequestTime > DEBOUNCE_INTERVAL) {
            fetchPostsUseCase.execute()
            lastRequestTime = currentTime
        }
    }
}