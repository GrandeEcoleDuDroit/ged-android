package com.upsaclay.message.domain.usecase

import com.upsaclay.common.domain.extensions.toEpochMilliUTC
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.entity.MessageNotification
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageNotificationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val messageNotificationRepository: MessageNotificationRepository,
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
                if (conversation.state == Conversation.ConversationState.DRAFT) {
                    conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.ERROR))
                }
                messageRepository.updateLocalMessage(message.copy(state = MessageState.ERROR))
            }
        }
    }

    private suspend fun createDataLocally(conversation: Conversation, message: Message) {
        if (conversation.state == Conversation.ConversationState.DRAFT) {
            conversationRepository.createLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATING))
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
                val messageNotification = MessageNotification(
                    conversation = conversation,
                    message = MessageNotification.Message(
                        messageId = message.id,
                        content = message.content,
                        timestamp = message.date.toEpochMilliUTC(),
                    )
                )
                messageNotificationRepository.sendNotification(it, messageNotification)
            }
        }
    }
}