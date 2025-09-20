package com.upsaclay.news.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: RefreshAnnouncementUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { connectivityObserver.isConnected } returns true

        useCase = RefreshAnnouncementUseCase(
            announcementRepository = announcementRepository,
            blockedUserRepository = blockedUserRepository,
            connectivityObserver = connectivityObserver,
            scope = testScope
        )
    }

    @Test
    fun refreshAnnouncement_should_refresh_when_debounce_interval_exceeded() = runTest {
        // When
        useCase()

        // Then
        coVerify { useCase.lastRequestTime > 0 }
    }

    @Test
    fun refreshAnnouncement_should_not_refresh_when_debounce_interval_not_exceeded() = runTest {
        // Given
        val currentTime = System.currentTimeMillis()
        useCase.lastRequestTime = currentTime

        // When
        useCase()

        // Then
        coVerify { useCase.lastRequestTime == currentTime }
    }

    @Test(expected = NoInternetConnectionException::class)
    fun refreshAnnouncement_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase()
    }
}