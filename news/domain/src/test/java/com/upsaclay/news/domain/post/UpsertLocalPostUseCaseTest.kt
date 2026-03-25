package com.upsaclay.news.domain.post

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.news.domain.post.usecase.UpsertLocalPostUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpsertLocalPostUseCaseTest {
    private val postRepository: PostRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpsertLocalPostUseCase

    @Before
    fun setUp() {
        coEvery { postRepository.getLocalPost(any()) } returns postFixture
        coEvery { postRepository.upsertLocalPost(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit
        coEvery { imageRepository.deleteCacheImage(any()) } returns Unit

        useCase = UpsertLocalPostUseCase(
            postRepository = postRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun upsertMissionUseCase_should_upsert_mission() = runTest {
        // When
        useCase.execute(postFixture)

        // Then
        coVerify { postRepository.upsertLocalPost(postFixture) }
    }

    @Test
    fun upsertMissionUseCase_should_delete_local_image_when_present() = runTest {
        // Given
        val imagePaths = listOf("path")
        val mission = postFixture.copy(state = Post.PostState.Publishing(imagePaths))
        coEvery { postRepository.getLocalPost(any()) } returns mission

        // When
        useCase.execute(postFixture)

        // Then
        coVerify { imageRepository.deleteLocalImage(imagePaths[0]) }
    }
}