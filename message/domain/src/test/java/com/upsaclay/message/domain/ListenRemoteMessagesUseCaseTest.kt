package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.userFixture
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
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ListenRemoteMessagesUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()

    private lateinit var useCase: ListenRemoteMessagesUseCase

    @Before
    fun setUp() {
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
    fun start_should_stop_and_replace_previous_message_listening_of_conversation() = runTest {
        // Given
        val conversation = conversationFixture
        val job = Job()
        useCase.jobs[conversation.interlocutor.id] = job

        // When
        useCase.start(conversation)

        // Then
        assert(job.isCancelled)
        assert(useCase.jobs[conversation.interlocutor.id] != job)
    }

    @Test
    fun listenRemoteMessages_should_listen_messages_with_last_message_date_offset_when_greater_than_effectiveFrom() = runTest {
        // Given
        val conversation = conversationFixture.copy(effectiveFrom = LocalDateTime.now().minusDays(1))
        val lastMessage = messageFixture.copy(date = LocalDateTime.now())
        coEvery { messageRepository.getLastMessage(any()) } returns lastMessage

        // When
        useCase.listenRemoteMessages(conversation)

        // Then
        coVerify {
            messageRepository.fetchRemoteMessages(
                conversation.id,
                conversation.interlocutor.id,
                lastMessage.date
            )
        }
    }

    @Test
    fun listenRemoteMessages_should_listen_messages_with_effectiveFrom_offset_when_greater_than_last_message_date() = runTest {
        // Given
        val conversation = conversationFixture.copy(effectiveFrom = LocalDateTime.now())
        val lastMessage = messageFixture.copy(date = LocalDateTime.now().minusDays(1))
        coEvery { messageRepository.getLastMessage(any()) } returns lastMessage

        // When
        useCase.listenRemoteMessages(conversation)

        // Then
        coVerify {
            messageRepository.fetchRemoteMessages(
                conversation.id,
                conversation.interlocutor.id,
                conversation.effectiveFrom
            )
        }
    }

    @Test
    fun listenMessages_should_upsert_local_message() = runTest {
        // When
        useCase.listenRemoteMessages(conversationFixture)

        // Then
        coVerify { messageRepository.upsertLocalMessage(messageFixture) }
    }

    @Test
    fun stopAll_should_stop_all_listening() = runTest {
        // Given
        useCase.start(conversationFixture)
        advanceUntilIdle()
        val jobs = useCase.jobs

        // When
        useCase.stopAll()

        // Then
        assert(useCase.jobs.isEmpty())
        assert(jobs.all { it.value.isCancelled })
    }

    @Test
    fun stop_should_stop_message_listening_of_conversation() = runTest {
        // Given
        val conversation = conversationFixture
        val job = Job()
        useCase.jobs[conversation.interlocutor.id] = job

        // When
        useCase.stop(conversation.interlocutor.id)

        // Then
        assert(useCase.jobs.isEmpty())
        assert(job.isCancelled)
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
        coVerify(exactly = 0) { useCase.listenRemoteMessages(conversation) }
    }
}