package com.upsaclay.message

import androidx.paging.PagingData
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.fixtures.messageFixture
import com.upsaclay.message.domain.fixtures.messagesFixture
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.notification.MessageNotificationManager
import com.upsaclay.message.presentation.chat.ChatViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val conversationRepository: ConversationRepository = mockk()
    private val messageRepository: MessageRepository = mockk()
    private val blockedUserRepository: BlockedUserRepository = mockk()
    private val sendMessageUseCase: SendMessageUseCase = mockk()
    private val messageNotificationManager: MessageNotificationManager = mockk()
    private val deleteConversationUseCase: DeleteConversationUseCase = mockk()
    private val generateIdUseCase: GenerateIdUseCase = mockk()

    private lateinit var chatViewModel: ChatViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val blockedUserId = "blockedUserId"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { generateIdUseCase.execute() } returns "testId"
        every { conversationRepository.getConversationFlow(any()) } returns flowOf(
            conversationFixture
        )
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { userRepository.getLocalUser } returns userFixture
        every { messageRepository.getPagingMessages(any()) } returns flowOf(PagingData.from(
            messagesFixture
        ))
        every { messageRepository.getNewMessagesFlow(any(), any()) } returns flowOf(messageFixture)
        every { sendMessageUseCase.execute(any(), any(), any()) } returns Unit
        every { blockedUserRepository.blockedUsers } returns flowOf(emptyMap())
        coEvery { deleteConversationUseCase.execute(any(), any()) } returns Unit
        coEvery { messageRepository.setMessagesSeen(any(), any()) } returns Unit
        coEvery { messageNotificationManager.clearNotifications(any()) } returns Unit
        coEvery { messageRepository.deleteLocalMessages(any()) } returns Unit

        chatViewModel = ChatViewModel(
            conversation = conversationFixture,
            userRepository = userRepository,
            conversationRepository = conversationRepository,
            messageRepository = messageRepository,
            sendMessageUseCase = sendMessageUseCase,
            messageNotificationManager = messageNotificationManager,
            blockedUserRepository = blockedUserRepository,
            deleteConversationUseCase = deleteConversationUseCase,
            generateIdUseCase = generateIdUseCase
        )
    }

    @Test
    fun onTextChange_should_update_message_text() {
        // Given
        val text = "Hello"

        // When
        chatViewModel.onMessageTextChange(text)

        // Then
        assertEquals(text, chatViewModel.uiState.value.messageText)
    }

    @Test
    fun sendMessage_should_send_message() {
        // Given
        chatViewModel.onMessageTextChange("Hello")

        // When
        chatViewModel.sendMessage()

        // Then
        coVerify { sendMessageUseCase.execute(any(), any(), any()) }
    }

    @Test
    fun sendMessage_should_reset_text() {
        // Given
        chatViewModel.onMessageTextChange("Hello")

        // When
        chatViewModel.sendMessage()

        // Then
        assertEquals("", chatViewModel.uiState.value.messageText)
    }

    @Test
    fun unblockUser_should_unblock_user() {
        // Given
        coEvery { blockedUserRepository.removeBlockedUser(userFixture.id, blockedUserId) } returns Unit

        // When
        chatViewModel.unblockUser(blockedUserId)

        // Then
        coVerify { blockedUserRepository.removeBlockedUser(userFixture.id, blockedUserId) }
    }
}