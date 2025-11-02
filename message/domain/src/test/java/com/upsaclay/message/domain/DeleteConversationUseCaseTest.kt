package com.upsaclay.message.domain

import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class DeleteConversationUseCaseTest {
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()

    private lateinit var useCase: DeleteConversationUseCase

    @Before
    fun setUp() {
        coEvery { conversationRepository.updateLocalConversation(any()) } returns Unit
        coEvery { conversationRepository.deleteConversation(any(), any(), any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit

        useCase = DeleteConversationUseCase(
            conversationRepository = conversationRepository,
            messageRepository = messageRepository
        )
    }

    @Test
    fun deleteConversation_should_delete_conversation() = runTest {
        // When
        useCase(
            conversationFixture,
            userFixture.id
        )

        // Then
        coVerify { conversationRepository.deleteConversation(any(), userFixture.id, any()) }
    }

    @Test
    fun deleteConversation_should_update_state_to_deleting() = runTest {
        // When
        useCase(
            conversationFixture,
            userFixture.id
        )

        // Then
        coVerify {
            conversationRepository.updateLocalConversation(
                conversationFixture.copy(state = Conversation.ConversationState.DELETING)
            )
        }
    }

    @Test
    fun deleteConversation_should_delete_local_conversation_messages() = runTest {
        // When
        useCase(conversationFixture, userFixture.id)

        // Then
        coVerify { messageRepository.deleteLocalMessages(any()) }
    }

    @Test
    fun deleteConversation_should_update_state_to_error_when_exception_thrown() = runTest {
        // Given
        coEvery { conversationRepository.deleteConversation(any(), any(), any()) } throws Exception()

        // When
        assertFailsWith<Exception> {
            useCase(
                conversationFixture,
                userFixture.id
            )
        }

        // Then
        coVerify {
            conversationRepository.updateLocalConversation(
                conversationFixture.copy(state = Conversation.ConversationState.ERROR)
            )
        }
    }
}