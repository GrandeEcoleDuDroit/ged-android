package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.domain.announcement.announcementFixture
import com.upsaclay.news.domain.announcement.announcementReportFixture
import com.upsaclay.news.domain.announcement.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.presentation.announcement.readannouncement.ReadAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

class ReadAnnouncementViewModelTest {
    private val announcementId = "announcementId"
    private val userRepository: UserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase = mockk()

    private lateinit var viewModel: ReadAnnouncementViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { announcementRepository.getLocalAnnouncementFlow(any()) } returns flowOf(announcementFixture)
        coEvery { deleteAnnouncementUseCase.execute(any()) } returns Unit
        coEvery { announcementRepository.reportAnnouncement(any()) } returns Unit

        viewModel = ReadAnnouncementViewModel(
            announcementId = announcementId,
            userRepository = userRepository,
            announcementRepository = announcementRepository,
            deleteAnnouncementUseCase = deleteAnnouncementUseCase
        )
    }

    @Test
    fun reportAnnouncement_should_report__announcement() {
        // Given
        val announcementReport = announcementReportFixture

        // When
        viewModel.reportAnnouncement(announcementReport)

        // Then
        coVerify { announcementRepository.reportAnnouncement(announcementReport) }
    }

    @Test
    fun deleteAnnouncement_should_delete_announcement() {
        // Given
        val announcement = announcementFixture

        // When
        viewModel.deleteAnnouncement()

        // Then
        coVerify { deleteAnnouncementUseCase.execute(announcement) }
    }
}