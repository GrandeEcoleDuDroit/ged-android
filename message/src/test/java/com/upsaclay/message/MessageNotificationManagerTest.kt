package com.upsaclay.message

import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.MessagesNotification
import com.upsaclay.message.domain.mapper.toMessagesNotification
import com.upsaclay.message.domain.messageNotificationFixture
import com.upsaclay.message.domain.messageNotificationsFixture
import com.upsaclay.message.domain.repository.MessageNotificationRepository
import com.upsaclay.message.notification.MessageNotificationManager
import com.upsaclay.message.notification.MessageNotificationPresenter
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MessageNotificationManagerTest {
    private val routeRepository: RouteRepository = mockk()
    private val messageNotificationRepository: MessageNotificationRepository = mockk()
    private val messageNotificationPresenter: MessageNotificationPresenter = mockk()

    private lateinit var manager: MessageNotificationManager

    @Before
    fun setUp() {
        every { routeRepository.currentRoute } returns ConversationRoute
        every { messageNotificationPresenter.start() } returns Unit
        every { messageNotificationPresenter.clearNotification(any()) } returns Unit
        coEvery { messageNotificationRepository.storeMessageNotification(any()) } returns Unit
        coEvery { messageNotificationRepository.getMessageNotifications(any()) } returns listOf(messageNotificationFixture)
        coEvery { messageNotificationRepository.deleteMessageNotifications(any()) } returns Unit
        coEvery { messageNotificationPresenter.showNotification(any()) } returns Unit

        manager = MessageNotificationManager(
            routeRepository = routeRepository,
            messageNotificationRepository = messageNotificationRepository,
            messageNotificationPresenter = messageNotificationPresenter
        )
    }

    @Test
    fun start_should_start_notification_presenter() {
        // When
        manager.start()

        // Then
        coVerify { messageNotificationPresenter.start() }
    }

    @Test
    fun showNotification_should_store_notification() = runTest {
        // Given
        val messageNotification = messageNotificationsFixture.first()

        // When
        manager.showNotification(messageNotification)

        // Then
        coVerify { messageNotificationRepository.storeMessageNotification(messageNotification) }
    }

    @Test
    fun showNotification_should_not_show_notification_when_current_screen_is_message() = runTest {
        // Given
        // Given
        val messagesNotification = MessagesNotification(
            conversation = messageNotificationFixture.conversation,
            messages = listOf(messageNotificationFixture.messageContent)
        )
        every { routeRepository.currentRoute } returns ChatRoute(
            conversationJson = ConversationJsonConverter.toConversationJson(messageNotificationFixture.conversation)
        )

        // When
        manager.showNotification(messageNotificationFixture)

        // Then
        coVerify(exactly = 0) { messageNotificationPresenter.showNotification(messagesNotification) }
    }

    @Test
    fun showNotification_should_show_notification_when_current_screen_is_not_message() = runTest {
        // Given
        val messagesNotification = MessagesNotification(
            conversation = messageNotificationFixture.conversation,
            messages = listOf(messageNotificationFixture.messageContent)
        )

        // When
        manager.showNotification(messageNotificationFixture)

        // Then
        coVerify { messageNotificationPresenter.showNotification(messagesNotification) }
    }

    @Test
    fun showNotification_should_show_stored_message_notifications() = runTest {
        // Given
        val messageNotifications = listOf(messageNotificationFixture).toMessagesNotification()

        // When
        manager.showNotification(messageNotificationFixture)

        // Then
        coVerify { messageNotificationRepository.getMessageNotifications(messageNotificationFixture.conversation.id) }
        coVerify { messageNotificationPresenter.showNotification(messageNotifications[0]) }
    }

    @Test
    fun clearNotifications_should_delete_local_message_notifications() = runTest {
        // Given
        val conversationId = messageNotificationFixture.conversation.id

        // When
        manager.clearNotifications(conversationId)

        // Then
        coVerify { messageNotificationRepository.deleteMessageNotifications(conversationId) }
        coVerify { messageNotificationPresenter.clearNotification(conversationId) }
    }

    @Test
    fun clearNotifications_should_clear_notifications() = runTest {
        // Given
        val conversationId = messageNotificationFixture.conversation.id

        // When
        manager.clearNotifications(conversationId)

        // Then
        coVerify { messageNotificationPresenter.clearNotification(conversationId) }
    }
}