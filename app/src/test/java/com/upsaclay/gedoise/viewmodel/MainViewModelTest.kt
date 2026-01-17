package com.upsaclay.gedoise.viewmodel

import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.ListenDataUseCase
import com.upsaclay.app.domain.usecase.SynchronizeDataUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.presentation.MainViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val listenDataUseCase: ListenDataUseCase = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val synchronizeDataUseCase: SynchronizeDataUseCase = mockk()
    private val fcmTokenUseCase: FcmTokenUseCase = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { authenticationRepository.authenticationState } returns flowOf(true)
        every { listenDataUseCase.start() } returns Unit
        every { listenDataUseCase.stop() } returns Unit
        coEvery { clearDataUseCase() } returns Unit
        coEvery { synchronizeDataUseCase() } returns Unit

        mainViewModel = MainViewModel(
            authenticationRepository = authenticationRepository,
            clearDataUseCase = clearDataUseCase,
            synchronizeDataUseCase = synchronizeDataUseCase,
            fcmTokenUseCase = fcmTokenUseCase,
            listenDataUseCase = listenDataUseCase
        )
    }

    @Test
    fun data_should_be_listened_when_user_is_authenticated() {
        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenDataUseCase.start() }
    }

    @Test
    fun data_should_be_synchronized_when_user_is_authenticated() {
        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { synchronizeDataUseCase() }
    }

    @Test
    fun data_should_stop_be_listened_when_user_is_unauthenticated() {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenDataUseCase.stop() }
    }

    @Test
    fun data_should_be_deleted_when_user_is_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()
        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }
}