package com.upsaclay.gedoise.viewmodel

import com.upsaclay.app.domain.usecase.ClearDataUseCase
import com.upsaclay.app.domain.usecase.FcmTokenUseCase
import com.upsaclay.app.domain.usecase.ListenBlockedUserEventsUseCase
import com.upsaclay.app.domain.usecase.ListenRemoteUserUseCase
import com.upsaclay.app.domain.usecase.SynchronizeDataUseCase
import com.upsaclay.authentication.domain.repository.AuthenticationRepository
import com.upsaclay.gedoise.presentation.MainViewModel
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
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
    private val authenticationRepository: AuthenticationRepository = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val synchronizeDataUseCase: SynchronizeDataUseCase = mockk()
    private val fcmTokenUseCase: FcmTokenUseCase = mockk()
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase = mockk()
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase = mockk()
    private val listenBlockedUserEventsUseCase: ListenBlockedUserEventsUseCase = mockk()

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { authenticationRepository.authenticationState } returns flowOf(true)
        coEvery { clearDataUseCase() } returns Unit
        coEvery { listenRemoteUserUseCase.start() } returns Unit
        coEvery { listenRemoteConversationsUseCase.start() } returns Unit
        coEvery { listenBlockedUserEventsUseCase.start() } returns Unit
        coEvery { listenRemoteMessagesUseCase.stopAll() } returns Unit
        coEvery { synchronizeDataUseCase() } returns Unit

        viewModel = MainViewModel(
            authenticationRepository = authenticationRepository,
            clearDataUseCase = clearDataUseCase,
            synchronizeDataUseCase = synchronizeDataUseCase,
            fcmTokenUseCase = fcmTokenUseCase,
            listenRemoteUserUseCase = listenRemoteUserUseCase,
            listenRemoteConversationsUseCase = listenRemoteConversationsUseCase,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            listenBlockedUserEventsUseCase = listenBlockedUserEventsUseCase
        )
    }

    @Test
    fun data_should_be_listened_when_user_is_authenticated() = runTest(testDispatcher) {
        // When
        viewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteUserUseCase.start() }
        coVerify { listenRemoteConversationsUseCase.start() }
        coVerify { listenBlockedUserEventsUseCase.start() }
        assert(viewModel.dataListeningJob?.isActive ?: false)
    }

    @Test
    fun data_should_be_synchronized_when_user_is_authenticated() {
        // When
        viewModel.updateDataOnAuthChange()

        // Then
        coVerify { synchronizeDataUseCase() }
    }

    @Test
    fun data_should_stop_be_listened_when_user_is_unauthenticated() {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(false)

        // When
        viewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteMessagesUseCase.stopAll() }
        assert(viewModel.dataListeningJob?.isCancelled ?: true)
    }

    @Test
    fun data_should_be_deleted_when_user_is_unauthenticated() = runTest {
        // Given
        every { authenticationRepository.authenticationState } returns flowOf(false)

        // When
        viewModel.updateDataOnAuthChange()
        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }
}