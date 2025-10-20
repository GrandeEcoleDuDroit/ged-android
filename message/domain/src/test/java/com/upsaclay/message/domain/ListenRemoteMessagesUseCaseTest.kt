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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListenMessagesRemoteMessagesUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()

    private lateinit var useCase: ListenRemoteMessagesUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.upsertLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.fetchRemoteConversations(any()) } returns flowOf(conversationFixture)
        coEvery { messageRepository.getLastMessage(any()) } returns messageFixture
        coEvery { messageRepository.fetchRemoteMessages(any(), any(), any()) } returns flowOf(messageFixture)
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns emptySet()

        useCase = ListenRemoteMessagesUseCase(
            messageRepository = messageRepository,
            blockedUserRepository = blockedUserRepository
        )
    }

    @Test
    fun listenRemoteMessages_should_not_listen_messages_same_conversation_twice() = runTest {
        // Given
        val conversation = conversationFixture
        useCase.jobs = mutableMapOf(conversation.interlocutor.id to Job())

        // When
        useCase.start(conversation)

        // Then
        assert(useCase.jobs.count() == 1)
    }

    @Test
    fun listenRemoteMessages_should_listen_messages_message_with_last_message_date_offset() = runTest {
        // When
        useCase.listenMessages(conversationFixture)

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
    fun listenMessages_should_upsert_local_message() = runTest {
        // When
        useCase.listenMessages(conversationFixture)

        // Then
        coVerify { messageRepository.upsertLocalMessage(messageFixture) }
    }

    @Test
    fun stopAll_should_stop_all_listening() = runTest {
        // Given
        useCase.start(conversationFixture)
        advanceUntilIdle()

        // When
        useCase.stopAll()

        // Then
        assert(useCase.jobs.isEmpty())
    }

    @Test
    fun stop_should_stop_listening_message_of_conversation() = runTest {
        // Given
        val conversation = conversationFixture
        useCase.jobs = mutableMapOf(conversation.interlocutor.id to Job())

        // When
        useCase.stop(conversation.interlocutor.id)

        // Then
        assert(useCase.jobs.isEmpty())
    }

    @Test
    fun listenRemoteMessages_should_not_listen_messages_of_blocked_users() = runTest {
        // Given
        val blockedUserId = "blockedUserId"
        val conversation = conversationFixture.copy(interlocutor = userFixture.copy(id = blockedUserId))
        coEvery { blockedUserRepository.getLocalBlockedUserIds() } returns setOf(blockedUserId)

        // When
        useCase.start(conversation)

        // Then
        coVerify(exactly = 0) { useCase.listenMessages(conversation) }
    }
}