package com.upsaclay.news.domain

import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.CreateAnnouncementUseCase
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
class CreateAnnouncementUseCaseTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val announcementJobQueue: AnnouncementJobQueue = mockk()

    private lateinit var useCase: CreateAnnouncementUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { announcementJobQueue.addJob(any(), any()) } returns Unit
        coEvery { announcementJobQueue.cancelAndRemoveJob(any()) } returns Unit
        coEvery { announcementRepository.createAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.updateAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.upsertLocalAnnouncement(any()) } returns Unit

        useCase = CreateAnnouncementUseCase(
            announcementRepository = announcementRepository,
            announcementJobQueue = announcementJobQueue,
            scope = testScope
        )
    }

    @Test
    fun createAnnouncement_should_create_announcement_with_publishing_state() = runTest {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.createAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHING))
        }
    }

    @Test
    fun createAnnouncement_should_update_local_announcement_to_published_state_when_succeeds() = runTest {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.PUBLISHED))
        }
    }

    @Test
    fun createAnnouncement_should_update_local_announcement_to_error_state_when_fails() = runTest {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)
        coEvery { announcementRepository.createAnnouncement(any()) } throws Exception()

        // When
        useCase(announcement)

        // Then
        coVerify {
            announcementRepository.upsertLocalAnnouncement(announcement.copy(state = AnnouncementState.ERROR))
        }
    }

    @Test
    fun createAnnouncement_should_store_job_reference() = runTest {
        // When
        useCase(announcementFixture)

        // Then
        coVerify {
            announcementJobQueue.addJob(any(), announcementFixture.id)
        }
    }

    @Test
    fun createAnnouncement_should_remove_job_reference_when_job_finished() = runTest {
        // When
        useCase(announcementFixture)

        // Then
        coVerify { announcementJobQueue.cancelAndRemoveJob(announcementFixture.id) }
    }
}