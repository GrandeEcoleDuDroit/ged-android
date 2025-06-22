package com.upsaclay.message

import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.NotificationMessages
import com.upsaclay.message.domain.mapper.toNotificationMessages
import com.upsaclay.message.domain.notificationMessageFixture
import com.upsaclay.message.domain.notificationMessageListFixture
import com.upsaclay.message.domain.repository.NotificationMessageRepository
import com.upsaclay.message.notification.NotificationMessageManager
import com.upsaclay.message.notification.NotificationMessagePresenter
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class NotificationManagerTest {
    private val routeRepository: RouteRepository = mockk()
    private val notificationMessageRepository: NotificationMessageRepository = mockk()
    private val notificationMessagePresenter: NotificationMessagePresenter = mockk()

    private lateinit var manager: NotificationMessageManager

    @Before
    fun setUp() {
        every { routeRepository.currentRoute } returns ConversationRoute
        every { notificationMessagePresenter.start() } returns Unit
        every { notificationMessagePresenter.clearNotification(any()) } returns Unit
        coEvery { notificationMessageRepository.storeNotificationMessage(any()) } returns Unit
        coEvery { notificationMessageRepository.getNotificationMessages(any()) } returns listOf(notificationMessageFixture)
        coEvery { notificationMessageRepository.deleteNotificationMessages(any()) } returns Unit
        coEvery { notificationMessagePresenter.showNotification(any()) } returns Unit

        manager = NotificationMessageManager(
            routeRepository = routeRepository,
            notificationMessageRepository = notificationMessageRepository,
            notificationMessagePresenter = notificationMessagePresenter
        )
    }

    @Test
    fun start_should_start_notification_presenter() {
        // When
        manager.start()

        // Then
        coVerify { notificationMessagePresenter.start() }
    }

    @Test
    fun showNotification_should_store_notification() = runTest {
        // Given
        val notificationMessage = notificationMessageListFixture.first()

        // When
        manager.showNotification(notificationMessage)

        // Then
        coVerify { notificationMessageRepository.storeNotificationMessage(notificationMessage) }
    }

    @Test
    fun showNotification_should_not_show_notification_when_current_screen_is_message() = runTest {
        // Given
        // Given
        val notificationMessages = NotificationMessages(
            conversation = notificationMessageFixture.conversation,
            messages = listOf(notificationMessageFixture.message)
        )
        every { routeRepository.currentRoute } returns ChatRoute(
            conversationJson = ConversationJsonConverter.toConversationJson(notificationMessageFixture.conversation)
        )

        // When
        manager.showNotification(notificationMessageFixture)

        // Then
        coVerify(exactly = 0) { notificationMessagePresenter.showNotification(notificationMessages) }
    }

    @Test
    fun showNotification_should_show_notification_when_current_screen_is_not_message() = runTest {
        // Given
        val notificationMessages = NotificationMessages(
            conversation = notificationMessageFixture.conversation,
            messages = listOf(notificationMessageFixture.message)
        )

        // When
        manager.showNotification(notificationMessageFixture)

        // Then
        coVerify { notificationMessagePresenter.showNotification(notificationMessages) }
    }

    @Test
    fun showNotification_should_show_stored_notification_messages() = runTest {
        // Given
        val notificationMessages = listOf(notificationMessageFixture).toNotificationMessages()

        // When
        manager.showNotification(notificationMessageFixture)

        // Then
        coVerify { notificationMessageRepository.getNotificationMessages(notificationMessageFixture.conversation.id) }
        coVerify { notificationMessagePresenter.showNotification(notificationMessages[0]) }
    }

    @Test
    fun clearNotifications_should_delete_local_notification_messages() = runTest {
        // Given
        val conversationId = notificationMessageFixture.conversation.id

        // When
        manager.clearNotifications(conversationId)

        // Then
        coVerify { notificationMessageRepository.deleteNotificationMessages(conversationId) }
        coVerify { notificationMessagePresenter.clearNotification(conversationId) }
    }

    @Test
    fun clearNotifications_should_clear_notification() = runTest {
        // Given
        val conversationId = notificationMessageFixture.conversation.id

        // When
        manager.clearNotifications(conversationId)

        // Then
        coVerify { notificationMessagePresenter.clearNotification(conversationId) }
    }
}