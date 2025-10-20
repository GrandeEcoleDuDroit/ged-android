package com.upsaclay.gedoise.usecase

import com.upsaclay.app.domain.ListenBlockedUserEvents
import com.upsaclay.app.domain.ListenDataUseCase
import com.upsaclay.app.domain.ListenRemoteUserUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class ListenDataUseCaseTest {
    private val listenRemoteConversationsUseCase: ListenRemoteConversationsUseCase = mockk()
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()
    private val listenRemoteUserUseCase: ListenRemoteUserUseCase = mockk()
    private val listenBlockedUserEvents: ListenBlockedUserEvents = mockk()

    private lateinit var useCase: ListenDataUseCase

    @Before
    fun setUp() {
        coEvery { listenRemoteMessagesUseCase.stopAll() } returns Unit
        coEvery { listenRemoteConversationsUseCase.start() } returns Unit
        coEvery { listenRemoteUserUseCase.start() } returns Unit
        coEvery { listenBlockedUserEvents.start() } returns Unit

        useCase = ListenDataUseCase(
            listenRemoteConversationsUseCase = listenRemoteConversationsUseCase,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            listenRemoteUserUseCase = listenRemoteUserUseCase,
            listenBlockedUserEvents = listenBlockedUserEvents
        )
    }

    @Test
    fun start_should_start_listening_remote_data() = runTest {
        // When
        useCase.start()

        // Then
        coVerify { listenRemoteConversationsUseCase.start() }
        coVerify { listenRemoteUserUseCase.start() }
        coVerify { listenBlockedUserEvents.start() }
    }

    @Test
    fun stop_should_stop_listening_data() = runTest {
        // When
        useCase.stop()

        // Then
        coVerify { listenRemoteMessagesUseCase.stopAll() }
    }
}