package com.upsaclay.message.notification

import android.os.Bundle
import com.upsaclay.common.domain.usecase.NavigationRequestUseCase
import com.upsaclay.common.presentation.NotificationManager
import com.upsaclay.message.domain.converter.ConversationJsonParser
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.repository.MessageNotificationRepository
import com.upsaclay.message.presentation.chat.ChatRoute
import com.upsaclay.message.presentation.conversation.ConversationRoute
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MessageNotificationManager(
    private val messageNotificationRepository: MessageNotificationRepository,
    private val messageNotificationPresenter: MessageNotificationPresenter,
    private val navigationRequestUseCase: NavigationRequestUseCase,
    private val scope: CoroutineScope
): NotificationManager {
    override fun createNotificationChannel() {
        messageNotificationPresenter.createNotificationChannel()
    }

    override fun presentNotification(extra: Bundle) {
        val messageNotification = parseMessageNotification(extra) ?: return
        scope.launch {
            messageNotificationRepository.storeMessageNotification(messageNotification)
            messageNotificationPresenter.presentNotification(messageNotification)
        }
    }

    override fun onNotificationClick(extra: Bundle) {
        val extraConversationJson = extra.getString(CONVERSATION_ID_EXTRA)
        val messageNotification = parseMessageNotification(extra)

        when {
            extraConversationJson != null -> navigationRequestUseCase.navigate(listOf(ConversationRoute, ChatRoute(extraConversationJson)))

            messageNotification != null -> {
                val conversationJson = ConversationJsonParser.toJson(messageNotification.conversation)
                navigationRequestUseCase.navigate(listOf(ConversationRoute, ChatRoute(conversationJson)))
            }
        }
    }

    suspend fun clearNotifications(conversationId: String) {
        messageNotificationRepository.deleteMessageNotifications(conversationId)
        messageNotificationPresenter.clearNotification(conversationId)
    }

    private fun parseMessageNotification(extra: Bundle): MessageNotification? {
        return extra.getString("value")?.let { value  ->
            runCatching { messageNotificationRepository.parseNotification(value) }.getOrNull()
        }
    }
}