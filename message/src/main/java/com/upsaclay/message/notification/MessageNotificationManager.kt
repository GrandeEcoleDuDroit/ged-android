package com.upsaclay.message.notification

import com.upsaclay.common.domain.repository.RouteRepository
import com.upsaclay.message.domain.converter.ConversationJsonConverter
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.mapper.toMessagesNotification
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
        val messageNotifications = messageNotificationRepository
            .getMessageNotifications(messageNotification.conversation.id)
            .toMessagesNotification()

        if (!isCurrentMessageScreen(messageNotification.conversation.id)) {
            messageNotifications.forEach {
                messageNotificationPresenter.showNotification(it)
            }
        }
    }

    suspend fun clearNotifications(conversationId: String) {
        messageNotificationRepository.deleteMessageNotifications(conversationId)
        messageNotificationPresenter.clearNotification(conversationId)
    }

    private fun isCurrentMessageScreen(conversationId: String): Boolean {
        val messageScreen = routeRepository.currentRoute as? ChatRoute
        return messageScreen
            ?.conversationJson
            ?.let(ConversationJsonConverter::toConversation)
            ?.id == conversationId
    }
}