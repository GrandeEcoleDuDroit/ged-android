package com.upsaclay.news.domain

import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.DeleteAnnouncementUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val announcementJobQueue: AnnouncementJobQueue = mockk()

    private lateinit var useCase: DeleteAnnouncementUseCase

    @Before
    fun setUp() {
        coEvery { announcementJobQueue.addJob(any(), any()) } returns Unit
        coEvery { announcementJobQueue.cancelAndRemoveJob(any()) } returns Unit
        coEvery { announcementRepository.deleteAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit

        useCase = DeleteAnnouncementUseCase(
            announcementRepository = announcementRepository,
            announcementJobQueue = announcementJobQueue
        )
    }

    @Test
    fun deleteAnnouncement_should_delete_announcement_when_published() = runTest {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.PUBLISHED)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteAnnouncement(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_local_announcement_when_state_is_draft() = runTest {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_local_announcement_when_state_is_error() = runTest {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.ERROR)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_local_announcement_when_state_is_publishing() = runTest {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.PUBLISHING)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_remove_job_reference_when_state_is_publishing() = runTest {
        // Given
        val announcement = announcementFixture.copy(state = AnnouncementState.PUBLISHING)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementJobQueue.cancelAndRemoveJob(announcement.id) }
    }
}