package com.upsaclay.common.domain

import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateProfilePictureUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpdateProfilePictureUseCase

    private val uri = "uri"

    @Before
    fun setUp() {
        coEvery { imageRepository.uploadImage(any()) } returns Unit
        coEvery { imageRepository.deleteRemoteImage(any()) } returns Unit
        coEvery { userRepository.updateProfilePictureFileName(any(), any()) } returns Unit

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
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.uploadImage(any()) }
    }

    @Test
    fun updateProfilePictureUseCase_should_delete_previous_profile_picture_when_not_null() = runTest {
        // When
        useCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.deleteRemoteImage(userFixture.profilePictureUrl!!) }
    }

    @Test(expected = TimeoutCancellationException::class)
    fun updateProfilePictureUseCase_should_throw_TimeoutCancellationException_when_uploading_image_takes_more_than_15_seconds() = runTest {
        // Given
        coEvery { imageRepository.uploadImage(any()) } just awaits

        // When
        useCase(userFixture, uri)
    }
}