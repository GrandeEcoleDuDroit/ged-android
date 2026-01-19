package com.upsaclay.message.domain.usecase

import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SendMessageUseCase(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val sendMessageNotificationUseCase: SendMessageNotificationUseCase,
    private val scope: CoroutineScope
) {
    operator fun invoke(conversation: Conversation, message: Message, userId: String) {
        scope.launch {
            try {
                createDataLocally(conversation, message)
                createDataRemotely(conversation, message, userId)
                sendMessageNotificationUseCase(conversation, message)
            } catch (_: Exception) {
                if (conversation.state == Conversation.ConversationState.DRAFT) {
                    conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.ERROR))
                }
                messageRepository.upsertLocalMessage(message.copy(state = MessageState.ERROR))
            }
        }
    }

    private suspend fun createDataLocally(conversation: Conversation, message: Message) {
        if (conversation.state == Conversation.ConversationState.DRAFT) {
            conversationRepository.createLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATING))
        }

        when (message.state) {
            MessageState.DRAFT -> messageRepository.createLocalMessage(message.copy(state = MessageState.SENDING))
            MessageState.ERROR -> messageRepository.updateLocalMessage(message.copy(state = MessageState.SENDING))
            else -> Unit
        }
    }

    private suspend fun createDataRemotely(conversation: Conversation, message: Message, userId: String) {
        if (conversation.state == Conversation.ConversationState.DRAFT) {
            conversationRepository.createRemoteConversation(conversation, userId)
            conversationRepository.updateLocalConversation(conversation.copy(state = Conversation.ConversationState.CREATED))
        }

        messageRepository.createRemoteMessage(message)
        messageRepository.updateLocalMessage(message.copy(state = MessageState.SENT))
    }
}