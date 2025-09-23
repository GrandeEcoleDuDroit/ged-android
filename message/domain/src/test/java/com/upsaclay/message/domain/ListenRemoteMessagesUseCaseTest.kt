package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
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
class ListenRemoteMessagesUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()

    private lateinit var useCase: ListenRemoteMessagesUseCase

    private val testScope = TestScope(StandardTestDispatcher())

    @Before
    fun setUp() {
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(conversationFixture)
        coEvery { messageRepository.getLastMessage(any()) } returns messageFixture
        coEvery { messageRepository.fetchRemoteMessages(any(), any(), any()) } returns flowOf(messageFixture)
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit

        useCase = ListenRemoteMessagesUseCase(
            messageRepository = messageRepository,
            blockedUserRepository = blockedUserRepository,
            scope = testScope
        )
    }

    @Test
    fun listenRemoteMessages_should_not_listen_same_conversation_twice() = runTest {
        // Given
        val conversations = listOf(conversationFixture)
        useCase.listeningJobs = mutableMapOf(
            conversationFixture.id to Job()
        )

        // When
        useCase.start(conversations[0])

        // Then
        assert(useCase.listeningJobs.count() == 1)
    }

    @Test
    fun listenRemoteMessages_should_listen_message_with_last_message_date_offset() = runTest {
        // When
        useCase.listenRemoteMessages(conversationFixture)

        // Then
        coVerify {
            messageRepository.fetchRemoteMessages(
                conversationFixture.id,
                conversationFixture.interlocutor.id,
                messageFixture.date
            )
        }
    }

    @Test
    fun listenRemoteMessages_should_upsert_local_message() = runTest {
        // When
        useCase.listenRemoteMessages(conversationFixture)

        // Then
        coVerify { messageRepository.upsertLocalMessage(messageFixture) }
    }

    @Test
    fun stop_should_stop_listening() = runTest(testScope.testScheduler) {
        // Given
        useCase.start(conversationFixture)
        advanceUntilIdle()

        // When
        useCase.stop()

        // Then
        assert(useCase.listeningJobs.isEmpty())
    }

    @Test
    fun listenRemoteMessages_should_not_listen_blocked_users_messages() {
        // Given
        val blockedUserId = "blockedUserId"
        val conversation = conversationFixture.copy(interlocutor = userFixture.copy(id = blockedUserId))
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns setOf(blockedUserId)

        // When
        useCase.start(conversation)

        // Then
        coVerify(exactly = 0) { useCase.listenRemoteMessages(conversation) }
    }
}