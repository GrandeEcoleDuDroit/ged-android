package com.upsaclay.message.notification

import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.mapper.toNotificationMessages
import com.upsaclay.message.domain.repository.NotificationMessageRepository
import com.upsaclay.message.presentation.chat.ChatRoute

class NotificationMessageManager(
    private val routeRepository: RouteRepository,
    private val notificationMessageRepository: NotificationMessageRepository,
    private val notificationMessagePresenter: NotificationMessagePresenter
) {
    fun start() {
        notificationMessagePresenter.start()
    }

    suspend fun showNotification(notificationMessage: NotificationMessage) {
        notificationMessageRepository.storeNotificationMessage(notificationMessage)
        val notificationMessages = notificationMessageRepository
            .getNotificationMessages(notificationMessage.conversation.id)
            .toNotificationMessages()

        if (!isCurrentMessageScreen(notificationMessage.conversation.id)) {
            notificationMessages.forEach {
                notificationMessagePresenter.showNotification(it)
            }
        }
    }

    suspend fun clearNotifications(conversationId: String) {
        notificationMessageRepository.deleteNotificationMessages(conversationId)
        notificationMessagePresenter.clearNotification(conversationId)
    }

    private fun isCurrentMessageScreen(conversationId: String): Boolean {
        val messageScreen = routeRepository.currentRoute as? ChatRoute
        return messageScreen
            ?.conversationJson
            ?.let(ConversationJsonConverter::toConversation)
            ?.id == conversationId
    }
}