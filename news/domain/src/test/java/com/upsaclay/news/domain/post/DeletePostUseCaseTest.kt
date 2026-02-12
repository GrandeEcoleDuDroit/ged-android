package com.upsaclay.news.domain.post

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.usecase.DeletePostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test


class DeletePostUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: DeletePostUseCase
    private val imageUrls = listOf("imageUrl")
    private val imagePaths = listOf("imagePath")

    @Before
    fun setUp() {
        coEvery { postRepository.deletePost(any()) } returns Unit
        coEvery { postRepository.deleteLocalPost(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit

        useCase = DeletePostUseCase(
            postRepository = postRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun deletePostUseCase_should_delete_post_when_state_is_published() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Published(imageUrls))

        // When
        useCase.execute(post)

        // Then
        coVerify { postRepository.deletePost(post) }
    }

    @Test
    fun deletePostUseCase_should_delete_local_post_when_state_is_not_published() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Draft)

        // When
        useCase.execute(post)

        // Then
        coEvery { postRepository.deleteLocalPost(post) }
    }

    @Test
    fun deletePostUseCase_should_delete_local_images_when_state_is_not_published_and_image_path_is_not_null() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Error(imagePaths))

        // When
        useCase.execute(post)

        // Then
        imagePaths.forEach {
            coVerify { imageRepository.deleteLocalImage(it) }
        }
    }
}