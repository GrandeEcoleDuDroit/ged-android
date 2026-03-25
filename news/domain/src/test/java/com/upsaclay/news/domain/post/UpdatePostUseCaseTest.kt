package com.upsaclay.news.domain.post

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.Post.PostState
import com.upsaclay.news.domain.post.usecase.UpdatePostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class UpdatePostUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpdatePostUseCase
    private val imageReferences = listOf(
        ImageReference.ImageUrl("imageUrl"),
        ImageReference.ImageUri("imageUri")
    )
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { imageRepository.getFileExtension(any()) } returns ""
        coEvery { imageRepository.createCacheImage(any(), any()) } returns file
        coEvery { postRepository.updatePost(any(), any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit
        coEvery { imageRepository.deleteCacheImage(any()) } returns Unit

        useCase = UpdatePostUseCase(
            postRepository = postRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun updatePostUseCase_should_create_cache_images_when_image_uris_is_not_empty() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Published())
        val imageUris = imageReferences
            .filterIsInstance<ImageReference.ImageUri>()
            .map { it.value }

        // When
        useCase.execute(post, imageReferences)

        // Then
        imageUris.forEach { uri ->
            coVerify { imageRepository.createCacheImage(any(), uri) }
        }
    }

    @Test
    fun updatePostUseCase_should_delete_created_cache_images() = runTest {
        // Given
        val post = postFixture.copy(state = PostState.Published())

        // When
        useCase.execute(post, imageReferences)

        // Then
        coVerify { imageRepository.deleteCacheImage(any()) }
    }
}