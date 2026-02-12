package com.upsaclay.news.domain

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.PostRepository
import com.upsaclay.news.domain.post.postFixture
import com.upsaclay.news.domain.post.usecase.CreatePostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class CreatePostUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: CreatePostUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val imageUris = listOf("imageUri")
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { postRepository.createPost(any(), any()) } returns Unit
        coEvery { postRepository.upsertLocalPost(any()) } returns Unit
        coEvery { imageRepository.getFileExtension(any()) } returns ""
        coEvery { imageRepository.createLocalImage(any(), any()) } returns file

        useCase = CreatePostUseCase(
            postRepository = postRepository,
            imageRepository = imageRepository,
            scope = testScope
        )
    }

    @Test
    fun createPostUseCase_should_create_local_images_when_image_uris_is_not_empty() = runTest {
        // When
        useCase.execute(postFixture, imageUris)

        // Then
        imageUris.forEach { uri ->
            coVerify { imageRepository.createLocalImage(any(), uri) }
        }
    }

    @Test
    fun createPostUseCase_should_create_post_with_publishing_state() = runTest  {
        // Given
        val post = postFixture.copy(state = PostState.Draft)

        // When
        useCase.execute(post, emptyList())

        // Then
        coVerify {
            postRepository.createPost(post.copy(state = PostState.Publishing()), emptyList())
        }
    }

    @Test
    fun createPostUseCase_should_update_local_post_to_published_state_when_succeeds() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Draft)

        // When
        useCase.execute(post, emptyList())

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Published()))
        }
    }

    @Test
    fun createPostUseCase_should_update_local_post_to_error_state_when_fails() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Draft)
        coEvery { postRepository.createPost(any(), any()) } throws Exception()

        // When
        useCase.execute(post, emptyList())

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Error()))
        }
    }

    @Test
    fun createPostUseCase_should_delete_local_image_when_succeed() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Draft)

        // When
        useCase.execute(post, imageUris)

        // Then
        coVerify {
            imageRepository.deleteLocalImage(any())
        }
    }
}