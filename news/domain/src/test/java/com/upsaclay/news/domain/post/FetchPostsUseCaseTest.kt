package com.upsaclay.news.domain.post

import com.upsaclay.news.domain.post.usecase.FetchPostsUseCase
import com.upsaclay.news.domain.post.usecase.UpsertLocalPostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchPostsUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val upsertLocalPostUseCase: UpsertLocalPostUseCase = mockk()

    private lateinit var useCase: FetchPostsUseCase

    @Before
    fun setUp() {
        every { postRepository.posts } returns flowOf(postsFixture)
        coEvery { postRepository.getLocalPosts() } returns postsFixture
        coEvery { postRepository.getRemotePosts() } returns postsFixture
        coEvery { postRepository.upsertLocalPost(any()) } returns Unit
        coEvery { postRepository.deleteLocalPost(any()) } returns Unit
        coEvery { upsertLocalPostUseCase.execute(any()) } returns Unit

        useCase = FetchPostsUseCase(
            postRepository = postRepository,
            upsertLocalPostUseCase = upsertLocalPostUseCase
        )
    }

    @Test
    fun fetchPost_should_upsert_new_remote_posts() = runTest {
        // Given
        coEvery { postRepository.getLocalPosts() } returns emptyList()
        coEvery { postRepository.getRemotePosts() } returns listOf(postFixture)

        // When
        useCase.execute()

        // Then
        coVerify { upsertLocalPostUseCase.execute(postFixture) }
    }

    @Test
    fun fetchPost_should_delete_posts_non_present_in_remote() = runTest {
        // Given
        coEvery { postRepository.getLocalPosts() } returns listOf(postFixture)
        coEvery { postRepository.getRemotePosts() } returns emptyList()

        // When
        useCase.execute()

        // Then
        coVerify { postRepository.deleteLocalPost(postFixture) }
    }
}