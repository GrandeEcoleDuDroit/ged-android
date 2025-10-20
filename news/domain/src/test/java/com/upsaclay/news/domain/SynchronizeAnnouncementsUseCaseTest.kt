package com.upsaclay.news.domain

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.SynchronizeAnnouncementsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SynchronizeAnnouncementsUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()

    private lateinit var useCase: SynchronizeAnnouncementsUseCase
    private val blockedUserId = "blockedUserId"

    @Before
    fun setUp() {
        every { announcementRepository.announcements } returns flowOf(announcementsFixture)
        every { announcementRepository.currentAnnouncements } returns announcementsFixture
        coEvery { announcementRepository.upsertLocalAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.getRemoteAnnouncements() } returns announcementsFixture
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns setOf(blockedUserId)

        useCase = SynchronizeAnnouncementsUseCase(
            announcementRepository = announcementRepository,
            blockedUserRepository = blockedUserRepository
        )
    }

    @Test
    fun synchronizeAnnouncement_should_upsert_new_remote_announcement() = runTest {
        // Given
        every { announcementRepository.currentAnnouncements } returns emptyList()
        coEvery { announcementRepository.getRemoteAnnouncements() } returns listOf(announcementFixture)

        // When
        useCase()

        // Then
        coVerify { announcementRepository.upsertLocalAnnouncement(announcementFixture) }
    }

    @Test
    fun synchronizeAnnouncement_should_delete_announcements_non_present_in_remote() = runTest {
        // Given
        every { announcementRepository.currentAnnouncements } returns listOf(announcementFixture)
        coEvery { announcementRepository.getRemoteAnnouncements() } returns emptyList()

        // When
        useCase()

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcementFixture) }
    }

    @Test
    fun synchronizeAnnouncement_should_not_upsert_announcement_from_blocked_user() = runTest {
        // Given
        val announcement = announcementFixture.copy(author = userFixture.copy(id = blockedUserId))
        coEvery { announcementRepository.getRemoteAnnouncements() } returns listOf(announcement)
        every { announcementRepository.currentAnnouncements } returns emptyList()

        // When
        useCase()

        // Then
        coVerify(exactly = 0) { announcementRepository.upsertLocalAnnouncement(any()) }
    }

    @Test
    fun synchronizeAnnouncement_should_delete_announcements_of_blocked_users() = runTest {
        // Given
        val announcement = announcementFixture.copy(author = userFixture.copy(id = blockedUserId))
        every { announcementRepository.currentAnnouncements } returns listOf(announcement)
        coEvery { announcementRepository.getRemoteAnnouncements() } returns emptyList()

        // When
        useCase()

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }
}