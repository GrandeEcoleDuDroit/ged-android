package com.upsaclay.message.domain

import com.upsaclay.common.domain.blockedUserFixture
import com.upsaclay.common.domain.blockedUsersFixture
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.fixtures.messageFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.ListenRemoteMessagesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class ListenRemoteMessagesUseCaseTest {
    private val messageRepository: MessageRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val userRepository: UserRepository = mockk()

    private lateinit var useCase: ListenRemoteMessagesUseCase

    @Before
    fun setUp() {
        every { userRepository.getLocalUser } returns userFixture
        every { blockedUserRepository.currentBlockedUsers } returns blockedUsersFixture
        coEvery { userRepository.user } returns flowOf(userFixture)
        coEvery { conversationRepository.getConversationsFlow() } returns flowOf(listOf(
            conversationFixture
        ))
        coEvery { messageRepository.getLastMessage(any()) } returns messageFixture
        coEvery { messageRepository.fetchRemoteMessages(any(), any(), any()) } returns flowOf(
            messageFixture
        )
        coEvery { messageRepository.upsertLocalMessage(any()) } returns Unit
        coEvery { blockedUserRepository.getLocalBlockedUsers() } returns blockedUsersFixture

        useCase = ListenRemoteMessagesUseCase(
            messageRepository = messageRepository,
            blockedUserRepository = blockedUserRepository,
            conversationRepository = conversationRepository,
            userRepository = userRepository
        )
    }

    @Test
    fun conversation_message_should_not_be_listening_only_once() = runTest {
        // Given
        val job = Job()
        useCase.listeningJobs[conversationFixture.id] = job

        // When
        useCase.start(this)

        // Then
        assert(useCase.listeningJobs[conversationFixture.id] == job)
    }

    @Test
    fun listenRemoteMessages_should_listen_messages_with_last_message_date_offset_when_greater_than_effectiveFrom() = runTest {
        // Given
        val conversation = conversationFixture.copy(effectiveFrom = LocalDateTime.now().minusDays(1))
        val lastMessage = messageFixture.copy(date = LocalDateTime.now())
        coEvery { messageRepository.getLastMessage(any()) } returns lastMessage

        // When
        useCase.listenRemoteMessages(userFixture.id, conversation)

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
        useCase.listenRemoteMessages(userFixture.id, conversation)

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
        useCase.listenRemoteMessages(userFixture.id, conversationFixture)

        // Then
        coVerify { messageRepository.upsertLocalMessage(messageFixture) }
    }

    @Test
    fun listenRemoteMessages_should_not_store_hidden_message() = runTest {
        // Given
        val message = messageFixture.copy(visible = false)
        coEvery {
            messageRepository.fetchRemoteMessages(conversationFixture.id, userFixture.id, null)
        } returns flowOf(message)

        // When
        useCase.start(this)

        // Then
        coVerify(exactly = 0) { messageRepository.upsertLocalMessage(message) }
    }

    @Test
    fun listenRemoteMessages_should_hide_message_when_user_is_blocked() = runTest {
        // When
        useCase.start(this)

        // Then
        coVerify(exactly = 0) { messageRepository.updateMessageVisibility(messageFixture, blockedUserFixture.userId, false) }
    }
}