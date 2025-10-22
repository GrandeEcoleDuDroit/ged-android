package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationState
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.entity.NotificationMessage
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.repository.NotificationMessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val notificationMessageRepository: NotificationMessageRepository,
    private val userRepository: UserRepository,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, message: Message, userId: String) {
        scope.launch {
            try {
                createDataLocally(conversation, message)
                createDataRemotely(conversation, message, userId)
                sendNotification(conversation, message)
            } catch (_: Exception) {
                if (conversation.state == ConversationState.DRAFT) {
                    conversationRepository.updateLocalConversation(conversation.copy(state = ConversationState.ERROR))
                }
                messageRepository.updateLocalMessage(message.copy(state = MessageState.ERROR))
            }
        }
    }

    private suspend fun createDataLocally(conversation: Conversation, message: Message) {
        if (conversation.state == ConversationState.DRAFT) {
            conversationRepository.createLocalConversation(conversation.copy(state = ConversationState.CREATING))
        }
        if (message.state == MessageState.DRAFT) {
            messageRepository.createLocalMessage(message.copy(state = MessageState.SENDING))
        }
    }

    private suspend fun createDataRemotely(conversation: Conversation, message: Message, userId: String) {
        if (conversation.shouldBeCreated) {
            conversationRepository.createRemoteConversation(conversation, userId)
        }
        messageRepository.createRemoteMessage(message)
    }

    private suspend fun sendNotification(conversation: Conversation, message: Message) {
        runCatching {
            userRepository.currentUser?.let {
                val notificationMessage = NotificationMessage(
                    conversation,
                    NotificationMessage.MessageContent(
                        message.content,
                        message.date.toEpochMilliUTC()
                    )
                )
                notificationMessageRepository.sendNotification(it, notificationMessage)
            }
        }
    }
}