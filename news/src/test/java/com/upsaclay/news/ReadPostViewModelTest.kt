package com.upsaclay.news

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.news.domain.announcement.postReportFixture
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import com.upsaclay.news.presentation.post.readpost.ReadPostViewModel
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

class ReadPostViewModelTest {
    private val postId = "postId"
    private val userRepository: UserRepository = mockk()
    private val postRepository: PostRepository = mockk()
    private val deletePostUseCase: DeletePostUseCase = mockk()

    private lateinit var viewModel: ReadPostViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { postRepository.getLocalPostFlow(any()) } returns flowOf(postFixture)
        coEvery { deletePostUseCase.execute(any()) } returns Unit
        coEvery { postRepository.reportPost(any()) } returns Unit

        viewModel = ReadPostViewModel(
            postId = postId,
            userRepository = userRepository,
            postRepository = postRepository,
            deletePostUseCase = deletePostUseCase
        )
    }

    @Test
    fun reportPost_should_report__post() {
        // Given
        val postReport = postReportFixture

        // When
        viewModel.reportPost(postReport)

        // Then
        coVerify { postRepository.reportPost(postReport) }
    }

    @Test
    fun deletePost_should_delete_post() {
        // Given
        val post = postFixture

        // When
        viewModel.deletePost()

        // Then
        coVerify { deletePostUseCase.execute(post) }
    }
}