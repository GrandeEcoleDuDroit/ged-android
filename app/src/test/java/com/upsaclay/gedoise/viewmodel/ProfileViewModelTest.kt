package com.upsaclay.gedoise.viewmodel

import com.upsaclay.app.domain.usecase.LogoutUseCase
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.gedoise.presentation.profile.ProfileViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()

    private lateinit var profileViewModel: ProfileViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { logoutUseCase.execute() } returns Unit

        profileViewModel = ProfileViewModel(
            userRepository = userRepository,
            logoutUseCase = logoutUseCase
        )
    }

    @Test
    fun logout_should_logout_user() {
        // When
        profileViewModel.logout()

        // Then
        coVerify { logoutUseCase.execute() }
    }
}