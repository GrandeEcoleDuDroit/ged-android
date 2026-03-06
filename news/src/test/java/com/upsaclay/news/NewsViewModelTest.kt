package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.domain.announcement.announcementFixture
import com.upsaclay.news.domain.announcement.announcementReportFixture
import com.upsaclay.news.domain.announcement.announcementsFixture
import com.upsaclay.news.domain.announcement.postReportFixture
import com.upsaclay.news.domain.announcement.usecase.DeleteAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.RecreateAnnouncementUseCase
import com.upsaclay.news.domain.announcement.usecase.RefreshAnnouncementsUseCase
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.postsFixture
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import com.upsaclay.news.domain.post.usecase.RecreatePostUseCase
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
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {
    private val recreateAnnouncementUseCase: RecreateAnnouncementUseCase = mockk()
    private val deleteAnnouncementUseCase: DeleteAnnouncementUseCase = mockk()
    private val refreshAnnouncementsUseCase: RefreshAnnouncementsUseCase = mockk()
    private val announcementRepository: AnnouncementRepository = mockk()
    private val postRepository: PostRepository = mockk()
    private val deletePostUseCase: DeletePostUseCase = mockk()
    private val recreatePostUseCase: RecreatePostUseCase = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var newsViewModel: NewsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { announcementRepository.announcements } returns flowOf(announcementsFixture)
        every { postRepository.posts } returns flowOf(postsFixture)
        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { recreateAnnouncementUseCase.execute(any()) } returns Unit
        coEvery { refreshAnnouncementsUseCase.execute() } returns Unit
        coEvery { deleteAnnouncementUseCase.execute(any()) } returns Unit
        coEvery { recreatePostUseCase.execute(any()) } returns Unit
        coEvery { deletePostUseCase.execute(any()) } returns Unit

        newsViewModel = NewsViewModel(
            recreateAnnouncementUseCase = recreateAnnouncementUseCase,
            deleteAnnouncementUseCase = deleteAnnouncementUseCase,
            announcementRepository = announcementRepository,
            postRepository = postRepository,
            recreatePostUseCase = recreatePostUseCase,
            deletePostUseCase = deletePostUseCase,
            userRepository = userRepository
        )
    }

    @Test
    fun recreateAnnouncement_should_recreate_announcement() = runTest {
        // Given
        val announcement = announcementFixture

        // When
        newsViewModel.recreateAnnouncement(announcement)

        // Then
        coVerify { recreateAnnouncementUseCase.execute(announcement) }
    }

    @Test
    fun deleteAnnouncement_should_delete_announcement() = runTest {
        // Given
        val announcement = announcementFixture

        // When
        newsViewModel.deleteAnnouncement(announcement)

        // Then
        coVerify { deleteAnnouncementUseCase.execute(announcement) }
    }

    @Test
    fun reportAnnouncement_should_report_announcement() = runTest {
        // Given
        val announcementReport = announcementReportFixture

        // When
        newsViewModel.reportAnnouncement(announcementReport)

        // Then
        coVerify { announcementRepository.reportAnnouncement(announcementReport) }
    }

    @Test
    fun recreatePost_should_recreate_post() = runTest {
        // Given
        val post = postsFixture.first()

        // When
        newsViewModel.recreatePost(post)

        // Then
        coVerify { recreatePostUseCase.execute(post) }
    }

    @Test
    fun deletePost_should_delete_post() = runTest {
        // Given
        val post = postsFixture.first()

        // When
        newsViewModel.deletePost(post)

        // Then
        coVerify { deletePostUseCase.execute(post) }
    }

    @Test
    fun reportPost_should_report_post() = runTest {
        // Given
        val postReport = postReportFixture

        // When
        newsViewModel.reportPost(postReport)

        // Then
        coVerify { postRepository.reportPost(postReport) }
    }
}