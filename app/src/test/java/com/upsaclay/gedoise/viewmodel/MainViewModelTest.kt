package com.upsaclay.gedoise.viewmodel

import MainViewModel
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.gedoise.domain.usecase.ClearDataUseCase
import com.upsaclay.gedoise.domain.usecase.ListenRemoteDataUseCase
import com.upsaclay.gedoise.domain.usecase.SynchronizeDataUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val listenRemoteDataUseCase: ListenRemoteDataUseCase = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val synchronizeDataUseCase: SynchronizeDataUseCase = mockk()
    private val authenticationRepository: AuthenticationRepository = mockk()

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { authenticationRepository.authenticated } returns flowOf(true)
        coEvery { userRepository.getUsers() } returns usersFixture
        coEvery { userRepository.getUser(any()) } returns userFixture
        coEvery { userRepository.storeUser(any()) } returns Unit
        coEvery { userRepository.deleteCurrentUser() } returns Unit
        coEvery { listenRemoteDataUseCase.start() } returns Unit
        coEvery { listenRemoteDataUseCase.stop() } returns Unit
        coEvery { clearDataUseCase() } returns Unit
        coEvery { authenticationRepository.logout() } returns Unit
        coEvery { synchronizeDataUseCase.synchronize() } returns Unit

        mainViewModel = MainViewModel(
            authenticationRepository = authenticationRepository,
            listenRemoteDataUseCase = listenRemoteDataUseCase,
            clearDataUseCase = clearDataUseCase,
            synchronizeDataUseCase = synchronizeDataUseCase,
        )
    }

    @Test
    fun data_should_be_listened_when_user_is_authenticated() {
        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteDataUseCase.start() }
    }

    @Test
    fun data_should_be_synchronized_when_user_is_authenticated() {
        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { synchronizeDataUseCase.synchronize() }
    }

    @Test
    fun data_should_stop_be_listened_when_user_is_unauthenticated() {
        // Given
        every { authenticationRepository.authenticated } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteDataUseCase.stop() }
    }

    @Test
    fun data_should_be_deleted_when_user_is_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticated } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()
        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }
}