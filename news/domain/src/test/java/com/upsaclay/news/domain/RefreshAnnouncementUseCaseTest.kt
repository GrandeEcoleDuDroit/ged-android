package com.upsaclay.news.domain

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefreshAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var useCase: RefreshAnnouncementUseCase
    private val testScope = TestScope(StandardTestDispatcher())

    @Before
    fun setUp() {
        every { announcementRepository.announcements } returns flowOf(announcementsFixture)
        every { connectivityObserver.isConnected } returns true
        coEvery { announcementRepository.getRemoteAnnouncements() } returns emptyList()
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns emptySet()
        coEvery { announcementRepository.upsertLocalAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit

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
        assert(useCase.lastRequestTime > 0)
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
    }

    @Test
    fun refreshAnnouncement_should_not_upsert_announcement_from_blocked_user() = runTest {
        // Given
        val userid = "blockedUserId"
        val announcement = announcementFixture.copy(author = userFixture.copy(id = userid))
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns setOf(userid)
        coEvery { announcementRepository.getRemoteAnnouncements() } returns listOf(announcement)
        every { announcementRepository.announcements } returns flowOf(emptyList())

        // When
        useCase()

        // Then
        coVerify(exactly = 0) { announcementRepository.updateLocalAnnouncement(any()) }
    }

    @Test
    fun refreshAnnouncement_should_delete_announcements_of_blocked_users() = runTest {
        // Given
        val userid = "blockedUserId"
        val announcement = announcementFixture.copy(author = userFixture.copy(id = userid))
        every { announcementRepository.announcements } returns flowOf(listOf(announcement))
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns setOf(userid)

        // When
        useCase()
        advanceUntilIdle()

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }

    @Test(expected = NoInternetConnectionException::class)
    fun refreshAnnouncement_should_throw_NoInternetConnectionException_when_not_connected() = runTest {
        // Given
        every { connectivityObserver.isConnected } returns false

        // When
        useCase()
    }
}