package com.upsaclay.message.notification

import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.mapper.toNotificationsUi
import com.upsaclay.message.domain.repository.MessageNotificationRepository
import com.upsaclay.message.presentation.chat.ChatRoute

class MessageNotificationManager(
    private val routeRepository: RouteRepository,
    private val messageNotificationRepository: MessageNotificationRepository,
    private val messageNotificationPresenter: MessageNotificationPresenter
) {
    fun start() {
        messageNotificationPresenter.start()
    }

    suspend fun showNotification(messageNotification: MessageNotification) {
        messageNotificationRepository.storeMessageNotification(messageNotification)
        val messageNotificationsUi = messageNotificationRepository
            .getMessageNotifications(messageNotification.conversation.id)
            .toNotificationsUi()

        if (!isCurrentMessageScreen(messageNotification.conversation.id)) {
            messageNotificationsUi.forEach {
                messageNotificationPresenter.showNotification(it)
            }
        }
    }

    suspend fun clearNotifications(conversationId: String) {
        messageNotificationRepository.deleteMessageNotifications(conversationId)
        messageNotificationPresenter.clearNotification(conversationId)
    }

    private fun isCurrentMessageScreen(conversationId: String): Boolean {
        val messageScreen = routeRepository.currentRoute as? ChatRoute ?: return false
        return messageScreen
            .conversationJson
            .let(ConversationJsonConverter::toConversation)
            ?.id == conversationId
    }
}