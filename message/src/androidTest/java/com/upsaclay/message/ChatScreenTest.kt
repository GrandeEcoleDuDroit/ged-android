package com.upsaclay.message

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.paging.PagingData
import com.upsaclay.common.domain.userFixture
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.messageFixture
import com.upsaclay.message.domain.messageFixture2
import com.upsaclay.message.presentation.chat.ChatDestination
import com.upsaclay.message.presentation.chat.ChatViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val chatViewModel: ChatViewModel = mockk()
    private val uiState = ChatViewModel.ChatUiState(
        conversation = conversationFixture,
        messageText = "",
        loading = false,
        isUserBlocked = false,
        currentUser = userFixture
    )

    @Before
    fun setUp() {
        every { chatViewModel.uiState } returns MutableStateFlow(uiState)
        every { chatViewModel.event } returns MutableSharedFlow()
        every { chatViewModel.sendMessage() } returns Unit
        every { chatViewModel.messages } returns MutableStateFlow(PagingData.from(emptyList()))
    }

    @Test
    fun sentMessageItem_should_be_displayed_when_user_is_sender() {
        // Given
        every { chatViewModel.messages } returns MutableStateFlow(PagingData.from(listOf(messageFixture)))

        // When
        rule.setContent {
            ChatDestination(
                conversation = conversationFixture,
                onBackClick = {},
                onInterlocutorClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.chat_screen_send_message_item_tag) + 0)
            .assertExists()

    }

    @Test
    fun receiveMessageItem_should_be_displayed_user_is_sender() {
        // Given
        every { chatViewModel.messages } returns MutableStateFlow(PagingData.from(listOf(messageFixture2)))

        // When
        rule.setContent {
            ChatDestination(
                conversation = conversationFixture,
                onBackClick = {},
                onInterlocutorClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.chat_screen_receive_message_item_tag) + 0)
            .assertExists()
    }

    @Test
    fun messageBlockedUserIndicator_should_be_displayed_when_user_is_blocked() {
        // Given
        every { chatViewModel.uiState } returns MutableStateFlow(uiState.copy(isUserBlocked = true))

        // When
        rule.setContent {
            ChatDestination(
                conversation = conversationFixture,
                onBackClick = {},
                onInterlocutorClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.chat_screen_blocked_user_indicator_tag))
            .assertExists()
    }

    @Test
    fun messageInput_should_be_displayed_when_user_is_not_blocked() {
        // When
        rule.setContent {
            ChatDestination(
                conversation = conversationFixture,
                onBackClick = {},
                onInterlocutorClick = {},
                viewModel = chatViewModel
            )
        }

        // Then
        rule.onAllNodesWithTag(rule.activity.getString(R.string.chat_screen_message_input_tag))
            .filterToOne(isFocusable())
            .assertExists()
    }
}