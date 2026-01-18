package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.RecreateConversationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecreateConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()

    private lateinit var useCase: RecreateConversationUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { conversationRepository.updateLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } returns Unit

        useCase = RecreateConversationUseCase(
            conversationRepository = conversationRepository,
        )
    }

    @Test
    fun recreateConversationUseCase_should_update_conversation_to_creating() = runTest(testScope.testScheduler) {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { conversationRepository.updateLocalConversation(conversationFixture.copy(state = Conversation.ConversationState.CREATING)) }
    }

    @Test
    fun recreateConversationUseCase_should_create_conversation() = runTest(testScope.testScheduler) {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { conversationRepository.createRemoteConversation(conversationFixture, userFixture.id) }
    }

    @Test
    fun recreateConversationUseCase_should_update_conversation_to_created_when_success() = runTest(testScope.testScheduler) {
        // When
        useCase(conversationFixture, userFixture.id)
        advanceUntilIdle()

        // Then
        coVerify { conversationRepository.updateLocalConversation(conversationFixture.copy(state = Conversation.ConversationState.CREATED)) }
    }

    @Test
    fun recreateConversationUseCase_should_update_conversation_to_error_when_failed() = runTest(testScope.testScheduler) {
        // Given
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } throws Exception()

        // When
        useCase(conversationFixture, userFixture.id)
        advanceUntilIdle()

        // Then
        coVerify { conversationRepository.updateLocalConversation(conversationFixture.copy(state = Conversation.ConversationState.ERROR)) }
    }
}