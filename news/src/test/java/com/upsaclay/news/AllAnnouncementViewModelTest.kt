package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.domain.usecase.RefreshAnnouncementUseCase
import com.upsaclay.news.presentation.allAnnouncement.AllAnnouncementViewModel
import com.upsaclay.news.presentation.news.NewsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AllAnnouncementViewModelTest {

    private val refreshAnnouncementUseCase: RefreshAnnouncementUseCase = mockk()

    private val userRepository: UserRepository = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()

    private lateinit var allAnnouncementViewModel: AllAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { announcementRepository.announcements } returns flowOf(announcementsFixture)
        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { refreshAnnouncementUseCase() } returns Unit


        allAnnouncementViewModel = AllAnnouncementViewModel(
            refreshAnnouncementUseCase = refreshAnnouncementUseCase,
            announcementRepository = announcementRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun refresh_should_refresh_annoucements() = runTest {
        // When
        allAnnouncementViewModel.refreshAnnouncements()

        // Then
        coVerify { refreshAnnouncementUseCase() }
    }


}