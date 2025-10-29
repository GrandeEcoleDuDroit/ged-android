package com.upsaclay.gedoise.viewmodel

import com.upsaclay.gedoise.presentation.MainViewModel
import com.upsaclay.app.domain.ClearDataUseCase
import com.upsaclay.app.domain.FcmTokenUseCase
import com.upsaclay.app.domain.ListenBlockedUserEvents
import com.upsaclay.app.domain.ListenRemoteUserUseCase
import com.upsaclay.app.domain.SynchronizeDataUseCase
import com.upsaclay.authentication.domain.usecase.ListenAuthenticationStateUseCase
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
    private val listenAuthenticationStateUseCase: ListenAuthenticationStateUseCase = mockk()
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase = mockk()
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase = mockk()
    private val listenBlockedUserEvents: ListenBlockedUserEvents = mockk()
    private val clearDataUseCase: ClearDataUseCase = mockk()
    private val synchronizeDataUseCase: SynchronizeDataUseCase = mockk()
    private val fcmTokenUseCase: FcmTokenUseCase = mockk()

    private lateinit var mainViewModel: MainViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { listenAuthenticationStateUseCase.authenticated } returns flowOf(true)
        coEvery { clearDataUseCase() } returns Unit
        coEvery { synchronizeDataUseCase() } returns Unit
        coEvery { listenRemoteMessagesUseCase.stopAll() } returns Unit
        coEvery { listenRemoteConversationsUseCase.start() } returns Unit
        coEvery { listenRemoteUserUseCase.start() } returns Unit
        coEvery { listenBlockedUserEvents.start() } returns Unit

        mainViewModel = MainViewModel(
            listenAuthenticationStateUseCase = listenAuthenticationStateUseCase,
            listenRemoteConversationsUseCase = listenRemoteConversationsUseCase,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            listenRemoteUserUseCase = listenRemoteUserUseCase,
            listenBlockedUserEvents = listenBlockedUserEvents,
            clearDataUseCase = clearDataUseCase,
            synchronizeDataUseCase = synchronizeDataUseCase,
            fcmTokenUseCase = fcmTokenUseCase
        )
    }

    @Test
    fun data_should_be_listened_when_user_is_authenticated() {
        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteConversationsUseCase.start() }
        coVerify { listenRemoteUserUseCase.start() }
        coVerify { listenBlockedUserEvents.start() }
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
        every { listenAuthenticationStateUseCase.authenticated } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()

        // Then
        coVerify { listenRemoteMessagesUseCase.stopAll() }
    }

    @Test
    fun data_should_be_deleted_when_user_is_unauthenticated() = runTest {
        // Given
        every { listenAuthenticationStateUseCase.authenticated } returns flowOf(false)

        // When
        mainViewModel.updateDataOnAuthChange()
        advanceUntilIdle()

        // Then
        coVerify { clearDataUseCase() }
    }
}