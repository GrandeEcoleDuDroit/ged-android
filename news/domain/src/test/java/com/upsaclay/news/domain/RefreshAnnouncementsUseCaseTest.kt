package com.upsaclay.news.domain

import com.upsaclay.news.domain.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshAnnouncementsUseCaseTest {
    private val synchronizeAnnouncementsUseCase: SynchronizeAnnouncementsUseCase = mockk()

    private lateinit var useCase: RefreshAnnouncementsUseCase

    @Before
    fun setUp() {
        coEvery { synchronizeAnnouncementsUseCase() } returns Unit

        useCase = RefreshAnnouncementsUseCase(
            synchronizeAnnouncementsUseCase = synchronizeAnnouncementsUseCase
        )
    }

    @Test
    fun refreshAnnouncement_should_synchronize_announcements_when_debounce_interval_exceeded() = runTest {
        // When
        useCase()

        // Then
        assert(useCase.lastRequestTime > 0)
        coVerify { synchronizeAnnouncementsUseCase() }
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
        coVerify(exactly = 0) { synchronizeAnnouncementsUseCase() }
    }
}