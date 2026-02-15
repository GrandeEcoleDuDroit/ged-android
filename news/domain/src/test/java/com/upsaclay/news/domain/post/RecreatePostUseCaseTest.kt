package com.upsaclay.news.domain.post

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.usecase.RecreatePostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class RecreatePostUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: RecreatePostUseCase
    private val files = listOf(File("file"))

    @Before
    fun setUp() {
        coEvery { postRepository.createPost(any(), any()) } returns Unit
        coEvery { postRepository.upsertLocalPost(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit

        useCase = RecreatePostUseCase(
            postRepository = postRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun recreatePostUseCase_should_recreate_post_when_state_is_error_only() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error())

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.createPost(any(), emptyList())
        }
    }

    @Test
    fun recreatePostUseCase_should_create_post_with_publishing_state() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error())

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.createPost(post.copy(state = PostState.Publishing()), emptyList())
        }
    }

    @Test
    fun recreatePostUseCase_should_create_post_with_publishing_state_and_image_paths_when_image_uris_is_provided() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error(files.map { it.path }))

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.createPost(post.copy(state = PostState.Publishing(files.map { it.path })), any())
        }
    }

    @Test
    fun recreatePostUseCase_should_update_local_post_to_published_state_when_succeeds() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error())

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Published()))
        }
    }

    @Test
    fun recreatePostUseCase_should_update_local_post_to_published_state_with_image_names_when_succeeds_and_image_uris_is_provided() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error(files.map { it.path }))

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Published(files.map { it.name })))
        }
    }

    @Test
    fun recreatePostUseCase_should_update_local_post_to_error_state_when_fails() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error())
        coEvery { postRepository.createPost(any(), any()) } throws Exception()

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Error()))
        }
    }

    @Test
    fun recreatePostUseCase_should_update_local_post_to_error_state_with_image_paths_when_fails_and_image_uris_is_provided() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error(files.map { it.path }))
        coEvery { postRepository.createPost(any(), any()) } throws Exception()

        // When
        useCase.execute(post)

        // Then
        coVerify {
            postRepository.upsertLocalPost(post.copy(state = PostState.Error(files.map { it.path })))
        }
    }

    @Test
    fun recreatePostUseCase_should_delete_local_image_when_succeed() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error(files.map { it.path }))

        // When
        useCase.execute(post)

        // Then
        files.forEach { file ->
            coVerify {
                imageRepository.deleteLocalImage(file.path)
            }
        }
    }
}