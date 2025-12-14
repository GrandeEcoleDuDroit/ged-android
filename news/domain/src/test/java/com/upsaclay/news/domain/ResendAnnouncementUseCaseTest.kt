package com.upsaclay.news.domain

import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.ResendAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ResendAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var useCase: ResendAnnouncementUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { announcementRepository.createAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.upsertLocalAnnouncement(any()) } returns Unit

        useCase = ResendAnnouncementUseCase(
            announcementRepository = announcementRepository,
            scope = testScope
        )
    }

    @Test
    fun resendAnnouncement_should_create_announcement_with_publishing_state() {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
        }
    }

    @Test
    fun resendAnnouncement_should_update_local_announcement_to_published_state_when_succeeds() {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.ERROR)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
        }
    }

    @Test
    fun resendAnnouncement_should_update_local_announcement_to_error_state_when_fails() {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.ERROR)
        coEvery { announcementRepository.createAnnouncement(any()) } throws Exception()

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
        }
    }
}