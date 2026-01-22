package com.upsaclay.message.domain

import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.Conversation.ConversationState
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.fixtures.messageFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.SendMessageNotificationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendMessageContentUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val sendMessageNotificationUseCase: SendMessageNotificationUseCase = mockk()

    private lateinit var useCase: SendMessageUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())

    @Before
    fun setUp() {
        every { userRepository.currentUser } returns userFixture
        coEvery { sendMessageNotificationUseCase.execute(any(), any()) } returns Unit
        coEvery { conversationRepository.updateLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } returns Unit
        coEvery { messageRepository.updateLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createLocalMessage(any()) } returns Unit
        coEvery { messageRepository.createRemoteMessage(any()) } returns Unit


        useCase = SendMessageUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            sendMessageNotificationUseCase = sendMessageNotificationUseCase,
            scope = testScope
        )
    }

    @Test
    fun sendMessageUseCase_should_create_local_conversation_with_creating_state_when_not_created() {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)

        // When
        useCase.execute(conversation, messageFixture, userFixture.id)

        // Then
        coVerify {
            conversationRepository.createLocalConversation(conversation.copy(state = ConversationState.CREATING))
        }
    }

    @Test
    fun sendMessageUseCase_should_update_local_conversation_state_to_error_state_when_creation_fails() {
        // Given
        val conversation = conversationFixture.copy(state = ConversationState.DRAFT)
        coEvery { conversationRepository.createRemoteConversation(any(), any()) } throws Exception()

        // When
        useCase.execute(conversation, messageFixture, userFixture.id)

        // Then
        coVerify { conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.ERROR)) }
    }

    @Test
    fun sendMessageUseCase_should_create_local_message_with_sending_state() = runTest {
        // Given
        val message = messageFixture.copy(state = MessageState.DRAFT)

        // When
        useCase.execute(conversationFixture, message, userFixture.id)

        // Then
        coVerify { messageRepository.createLocalMessage(message.copy(state = MessageState.SENDING)) }
    }

    @Test
    fun sendMessageUseCase_should_upsert_local_message_state_to_error_state_when_fails() {
        // Given
        val message = messageFixture.copy(state = MessageState.SENDING)
        coEvery { messageRepository.createRemoteMessage(any()) } throws Exception()

        // When
        useCase.execute(conversationFixture, message, userFixture.id)

        // Then
        coVerify { messageRepository.upsertLocalMessage(message.copy(state = MessageState.ERROR)) }
    }

    @Test
    fun sendMessageUseCase_should_send_notification() {
        // When
        useCase.execute(conversationFixture, messageFixture, userFixture.id)

        // Then
        coVerify { sendMessageNotificationUseCase.execute(conversationFixture, messageFixture) }
    }
}