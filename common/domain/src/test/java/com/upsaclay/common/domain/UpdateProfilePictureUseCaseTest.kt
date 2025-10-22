package com.upsaclay.common.domain

import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import io.mockk.awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class UpdateProfilePictureUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val imageRepository: ImageRepository = mockk()
    private val fileRepository: FileRepository = mockk()

    private lateinit var updateProfilePictureUseCase: UpdateProfilePictureUseCase

    private val file: File = File("file")
    private val uri = "uri"

    @Before
    fun setUp() {
        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { imageRepository.uploadImage(any()) } returns Unit
        coEvery { imageRepository.deleteImage(any()) } returns Unit
        coEvery { userRepository.updateProfilePictureFileName(any(), any()) } returns Unit
        coEvery { fileRepository.createCacheFile(any(), any()) } returns file

        updateProfilePictureUseCase = UpdateProfilePictureUseCase(
            fileRepository = fileRepository,
            imageRepository = imageRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun updateProfilePictureUseCase_should_update_profile_picture() = runTest {
        // When
        updateProfilePictureUseCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.uploadImage(any()) }
    }

    @Test
    fun updateProfilePictureUseCase_should_delete_previous_profile_picture_when_not_null() = runTest {
        // When
        updateProfilePictureUseCase(userFixture, uri)

        // Then
        coVerify { userRepository.updateProfilePictureFileName(userFixture.id, any()) }
        coVerify { imageRepository.deleteImage(userFixture.profilePictureUrl!!.substringAfterLast("/")) }
    }

    @Test(expected = TimeoutCancellationException::class)
    fun updateProfilePictureUseCase_should_throw_TimeoutCancellationException_when_uploading_image_takes_more_than_15_seconds() = runTest {
        // Given
        coEvery { imageRepository.uploadImage(any()) } just awaits

        // When
        updateProfilePictureUseCase(userFixture, uri)
    }
}