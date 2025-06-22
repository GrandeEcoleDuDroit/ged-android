package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteConversationUseCaseTest {
    private val userRepository: UserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val listenRemoteMessagesUseCase: ListenRemoteMessagesUseCase = mockk()

    private lateinit var useCase: ListenRemoteConversationsUseCase

    private val testScope = TestScope(StandardTestDispatcher())

    @Before
    fun setUp() {
        coEvery { userRepository.user } returns flowOf(userFixture)
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(conversationFixture)
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { listenRemoteMessagesUseCase.start(any()) } returns Unit

        useCase = ListenRemoteConversationsUseCase(
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            listenRemoteMessagesUseCase = listenRemoteMessagesUseCase,
            scope = testScope
        )
    }

    @Test
    fun listenRemoteConversationsUseCase_should_start_listenRemoteMessage() = runTest(testScope.testScheduler) {
        // When
        useCase.start()
        advanceUntilIdle()

        // Then
        coVerify { listenRemoteMessagesUseCase.start(conversationFixture) }
    }

    @Test
    fun listenRemoteConversationsUseCase_should_upsert_local_conversation() = runTest(testScope.testScheduler) {
        // When
        useCase.start()
        advanceUntilIdle()

        // Then
        coVerify { conversationRepository.upsertLocalConversation(conversationFixture) }
    }
}