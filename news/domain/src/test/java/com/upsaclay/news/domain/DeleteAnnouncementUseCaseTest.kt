package com.upsaclay.news.domain

import com.upsaclay.news.domain.entity.AnnouncementState
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

    private lateinit var useCase: DeleteAnnouncementUseCase

    @Before
    fun setUp() {
        coEvery { announcementRepository.deleteAnnouncement(any()) } returns Unit
        coEvery { announcementRepository.deleteLocalAnnouncement(any()) } returns Unit

        useCase = DeleteAnnouncementUseCase(
            announcementRepository = announcementRepository
        )
    }

    @Test
    fun deleteAnnouncement_should_delete_remote_announcement_when_published() = runTest {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.PUBLISHED)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteAnnouncement(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_local_announcement_when_not_published() = runTest {
        // Given
        val announcement = longAnnouncementFixture.copy(state = AnnouncementState.DRAFT)

        // When
        useCase(announcement)

        // Then
        coVerify { announcementRepository.deleteLocalAnnouncement(announcement) }
    }
}