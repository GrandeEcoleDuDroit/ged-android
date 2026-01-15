package com.upsaclay.common.domain

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class UpdateProfilePictureUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpdateProfilePictureUseCase

    private val uri = "uri"

    @Before
    fun setUp() {
        coEvery { imageRepository.createCacheImage(any(), any()) } returns File("path")
        coEvery { imageRepository.deleteCacheImage(any()) } returns Unit
        coEvery { userRepository.updateProfilePicture(any(), any(), any()) } returns Unit

        useCase = UpdateProfilePictureUseCase(
            userRepository = userRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun updateProfilePictureUseCase_should_update_profile_picture() = runTest {
        // When
        useCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePicture(userFixture, any(), any()) }
    }
}