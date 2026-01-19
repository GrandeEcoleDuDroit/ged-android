package com.upsaclay.news.domain

import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.FetchAnnouncementsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshAnnouncementsUseCaseTest {
    private val fetchAnnouncementsUseCase: FetchAnnouncementsUseCase = mockk()

    private lateinit var useCase: RefreshAnnouncementsUseCase

    @Before
    fun setUp() {
        coEvery { fetchAnnouncementsUseCase() } returns Unit

        useCase = RefreshAnnouncementsUseCase(
            fetchAnnouncementsUseCase = fetchAnnouncementsUseCase
        )
    }

    @Test
    fun refreshAnnouncement_should_synchronize_announcements_when_debounce_interval_exceeded() = runTest {
        // When
        useCase()

        // Then
        assert(useCase.lastRequestTime > 0)
        coVerify { fetchAnnouncementsUseCase() }
    }

    @Test
    fun refreshAnnouncement_should_not_refresh_when_debounce_interval_not_exceeded() = runTest {
        // Given
        val currentTime = System.currentTimeMillis()
        useCase.lastRequestTime = currentTime

        // When
        useCase()

        // Then
        assert(useCase.lastRequestTime == currentTime)
        coVerify(exactly = 0) { fetchAnnouncementsUseCase() }
    }
}