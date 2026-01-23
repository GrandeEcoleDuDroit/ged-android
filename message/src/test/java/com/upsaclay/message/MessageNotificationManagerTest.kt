package com.upsaclay.message

import android.os.Bundle
import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.common.domain.usecase.NavigationRequestUseCase
import com.upsaclay.message.domain.converter.ConversationJsonParser
import com.upsaclay.message.domain.fixtures.messageNotificationFixture
import com.upsaclay.message.domain.repository.MessageNotificationRepository
import com.upsaclay.message.notification.MessageNotificationManager
import com.upsaclay.message.notification.MessageNotificationPresenter
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MessageNotificationManagerTest {
    private val routeRepository: RouteRepository = mockk()
    private val messageNotificationRepository: MessageNotificationRepository = mockk()
    private val messageNotificationPresenter: MessageNotificationPresenter = mockk()
    private val navigationRequestUseCase: NavigationRequestUseCase = mockk()

    private lateinit var manager: MessageNotificationManager
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val bundle: Bundle = mockk()

    @Before
    fun setUp() {
        every { bundle.getString(any()) } returns ""
        every { navigationRequestUseCase.navigate(any()) } returns Unit
        every { routeRepository.currentRoute } returns ConversationRoute
        every { messageNotificationPresenter.createNotificationChannel() } returns Unit
        every { messageNotificationPresenter.clearNotification(any()) } returns Unit
        every { messageNotificationRepository.parseNotification(any()) } returns messageNotificationFixture
        coEvery { messageNotificationRepository.storeMessageNotification(any()) } returns Unit
        coEvery { messageNotificationRepository.getMessageNotifications(any()) } returns listOf(
            messageNotificationFixture
        )
        coEvery { messageNotificationRepository.deleteMessageNotifications(any()) } returns Unit

        manager = MessageNotificationManager(
            messageNotificationRepository = messageNotificationRepository,
            messageNotificationPresenter = messageNotificationPresenter,
            navigationRequestUseCase = navigationRequestUseCase,
            scope = testScope
        )
    }

    @Test
    fun createNotificationChannel_should_create_notification_channel() {
        // When
        manager.createNotificationChannel()

        // Then
        coVerify { messageNotificationPresenter.createNotificationChannel() }
    }

    @Test
    fun onNotificationClick_should_navigate_to_chat_route() {
        // Given
        val conversationJson = ConversationJsonParser.toJson(messageNotificationFixture.conversation)
        val route = listOf(ConversationRoute, ChatRoute(conversationJson))
        every { bundle.getString(any()) } returns conversationJson

        // When
        manager.onNotificationClick(bundle)

        // Then
        coVerify { navigationRequestUseCase.navigate(route) }
    }


    @Test
    fun presentNotification_should_store_notification() = runTest {
        // When
        manager.presentNotification(bundle)

        // Then
        coVerify { messageNotificationRepository.storeMessageNotification(messageNotificationFixture) }
    }

    @Test
    fun presentNotification_should_presentNotification() = runTest {
        // When
        manager.presentNotification(bundle)

        // Then
        coVerify { messageNotificationPresenter.presentNotification(messageNotificationFixture) }
    }

    @Test
    fun clearNotifications_should_delete_message_notifications() = runTest {
        // Given
        val conversationId = messageNotificationFixture.conversation.id

        // When
        manager.clearNotifications(conversationId)

        // Then
        coVerify { messageNotificationRepository.deleteMessageNotifications(conversationId) }
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