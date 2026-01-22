package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.message.domain.fixtures.conversationDTOFixture
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.mapper.toConversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.usecase.ListenRemoteConversationsUseCase
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        coEvery { userRepository.user } returns flowOf(userFixture)
        coEvery { userRepository.getUserFlow(any()) } returns flowOf(userFixture2)
        coEvery { conversationRepository.getRemoteConversationsFlow(any()) } returns flowOf(
            conversationDTOFixture
        )
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { listenRemoteMessagesUseCase.start(any()) } returns Unit
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit

        useCase = ListenRemoteConversationsUseCase(
            userRepository = userRepository,
            conversationRepository = conversationRepository
        )
    }

    @Test
    fun listenRemoteConversationsUseCase_should_get_interlocutor_from_memory_when_already_fetched() = runTest {
        // Given
        val participants = listOf(userFixture.id, userFixture2.id)
        val conversationDTO = conversationDTOFixture.copy(participants = participants)
        val conversation = conversationDTO.toConversation(userFixture2)
        coEvery { conversationRepository.getRemoteConversationsFlow(any()) } returns flowOf(conversationDTO)
        useCase.fetchedInterlocutors[userFixture2.id] = userFixture2

        // When
        useCase.start(this)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { userRepository.getUserFlow(any()) }
        coVerify { conversationRepository.upsertLocalConversation(conversation) }
    }

    @Test
    fun listenRemoteConversationsUseCase_should_fetch_interlocutor_when_not_fetched() = runTest {
        // Given
        val participants = listOf(userFixture.id, userFixture2.id)
        val conversationDTO = conversationDTOFixture.copy(participants = participants)
        val conversation = conversationDTO.toConversation(userFixture2)
        coEvery { conversationRepository.getRemoteConversationsFlow(any()) } returns flowOf(conversationDTO)
        coEvery { userRepository.getUserFlow(any()) } returns flowOf(userFixture2)

        // When
        useCase.start(this)
        advanceUntilIdle()

        // Then
        coVerify { userRepository.getUserFlow(userFixture2.id) }
        coVerify { conversationRepository.upsertLocalConversation(conversation) }
    }

    @Test
    fun listenRemoteConversationsUseCase_should_upsert_local_conversation() = runTest {
        // Given
        coEvery { conversationRepository.getConversation(any()) } returns null

        // When
        useCase.start(this)
        advanceUntilIdle()

        // Then
        coVerify { conversationRepository.upsertLocalConversation(conversationFixture) }
    }
}