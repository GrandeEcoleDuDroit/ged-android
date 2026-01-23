package com.upsaclay.gedoise.viewmodel

import android.net.Uri
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.UpdateProfilePictureUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.accountinformation.AccountInformationViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest {
    private val updateProfilePictureUseCase: UpdateProfilePictureUseCase = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var accountInformationViewModel: AccountInformationViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val uri: Uri = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { updateProfilePictureUseCase.execute(any(), any()) } returns Unit
        coEvery { userRepository.deleteProfilePicture(any()) } returns Unit

        accountInformationViewModel = AccountInformationViewModel(
            updateProfilePictureUseCase = updateProfilePictureUseCase,
            userRepository = userRepository
        )
    }

    @Test
    fun onProfilePictureUriChange_should_update_profile_picture_uri() {
        // When
        accountInformationViewModel.onProfilePictureUriChange(uri)

        // Then
        assert(accountInformationViewModel.uiState.value.profilePictureUri == uri)
    }

    @Test
    fun onScreenStateChange_should_update_screen_state() {
        // Given
        val screenState = AccountInformationViewModel.AccountInformationScreenState.EDIT

        // When
        accountInformationViewModel.onScreenStateChange(screenState)

        // Then
        assertEquals(screenState, accountInformationViewModel.uiState.value.screenState)
    }

    @Test
    fun updateUserProfilePicture_should_update_profile_picture_when_uri_is_not_null() = runTest {
        // Given
        accountInformationViewModel.onProfilePictureUriChange(uri)

        // When
        accountInformationViewModel.updateProfilePicture()

        // Then
        coVerify { updateProfilePictureUseCase.execute(any(), any()) }
    }

    @Test
    fun deleteProfilePicture_should_reset_profile_picture_uri() = runTest {
        // Given
        accountInformationViewModel.onProfilePictureUriChange(uri)

        // When
        accountInformationViewModel.deleteProfilePicture()

        // Then
        assertEquals(null, accountInformationViewModel.uiState.value.profilePictureUri)
    }
}