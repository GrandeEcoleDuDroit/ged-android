package com.upsaclay.authentication

import android.net.Uri
import com.upsaclay.authentication.domain.usecase.RegistrationUseCase
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateUserProfilePictureUseCase
import com.upsaclay.common.utils.userFixture
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RegistrationUseCaseTest {
    private lateinit var registrationUseCase: RegistrationUseCase
    private lateinit var userRepository: UserRepository
    private lateinit var updateUserProfilePictureUseCase: UpdateUserProfilePictureUseCase
    private lateinit var uri: Uri

    @Before
    fun setUp() {
        userRepository = mockk()
        updateUserProfilePictureUseCase = mockk()
        uri = mockk()
        registrationUseCase = RegistrationUseCase(userRepository, updateUserProfilePictureUseCase)

        coEvery { userRepository.createUser(any()) } returns Result.success(0)
        coEvery { updateUserProfilePictureUseCase(any(), any(), any()) } returns Result.success(Unit)
    }

    @Test
    fun registration_should_call_create_user() {
        runTest {
            registrationUseCase(userFixture, uri)
            coVerify(exactly = 1) { userRepository.createUser(any()) }
        }
    }

    @Test
    fun registration_should_update_profile_picture_url_if_not_null() {
        runTest {
            registrationUseCase(userFixture, uri)
            coVerify(exactly = 1) { updateUserProfilePictureUseCase(any(), any(), any()) }
        }
    }
}