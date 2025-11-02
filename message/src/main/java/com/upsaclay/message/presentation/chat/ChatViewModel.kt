package com.upsaclay.message.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.BlockedUserRepository
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.entity.MessageReport
import com.upsaclay.message.domain.repository.ConversationRepository
import com.upsaclay.message.domain.repository.MessageRepository
import com.upsaclay.message.domain.usecase.DeleteConversationUseCase
import com.upsaclay.message.domain.usecase.SendMessageUseCase
import com.upsaclay.message.notification.MessageNotificationManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

class ChatViewModel(
    private var conversation: Conversation,
    private val userRepository: UserRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val messageNotificationManager: MessageNotificationManager,
    private val blockedUserRepository: BlockedUserRepository,
    private val sendMessageUseCase: SendMessageUseCase,
    private val deleteConversationUseCase: DeleteConversationUseCase
): ViewModel() {
    private val user: User? = userRepository.currentUser
    private val _uiState = MutableStateFlow(
        ChatUiState(
            messageText = "",
            loading = false,
            userBlocked = false,
            currentUser = userRepository.currentUser
        )
    )
    internal val uiState: StateFlow<ChatUiState> = _uiState
    internal val messages: Flow<PagingData<Message>> = messageRepository.getPagingMessages(conversation.id)
    private val _event = MutableSharedFlow<SingleUiEvent>()
    internal val event: Flow<SingleUiEvent> = _event

    init {
        listenConversation()
        listenCurrentUser()
        listenBlockUserIds()

        emitNewMessageReceived()
        seeMessages()
        seeNewMessage()
        clearChatNotifications()
    }

    fun onMessageTextChange(text: String) {
        _uiState.update {
            it.copy(messageText = text)
        }
    }

    fun sendMessage() {
        try {
            val text = uiState.value.messageText.takeUnless { it.isEmpty() } ?: return
            val user = requireNotNull(user)
            val message = Message(
                id = GenerateIdUseCase.longId,
                conversationId = conversation.id,
                senderId = user.id,
                recipientId = conversation.interlocutor.id,
                content = text,
                date = LocalDateTime.now(ZoneOffset.UTC),
                state = MessageState.DRAFT
            )

            sendMessageUseCase(
                conversation = conversation,
                message = message,
                userId = user.id
            )
            _uiState.update { it.copy(messageText = "") }
        } catch (_: IllegalArgumentException) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(com.upsaclay.common.R.string.current_user_not_found_error))
            }
        }
    }

    fun resendMessage(message: Message) {
        try {
            val user = requireNotNull(user)
            viewModelScope.launch {
                sendMessageUseCase(
                    conversation = conversation,
                    message = message.copy(date = LocalDateTime.now(ZoneOffset.UTC)),
                    userId = user.id
                )
            }
        } catch (_: IllegalArgumentException) {
            viewModelScope.launch {
                _event.emit(SingleUiEvent.Error(com.upsaclay.common.R.string.current_user_not_found_error))
            }
        }
    }

    fun deleteMessage(message: Message) {
        viewModelScope.launch {
            messageRepository.deleteLocalMessage(message)
        }
    }

    fun reportMessage(report: MessageReport) {
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                messageRepository.reportMessage(report)
                _event.emit(MessageEvent.MessageReported)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun unblockUser(userId: String) {
        val currentUserId = uiState.value.currentUser?.id ?: return
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                blockedUserRepository.unblockUser(currentUserId, userId)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    fun deleteConversation() {
        val currentUserId = uiState.value.currentUser?.id ?: return
        _uiState.update { it.copy(loading = true) }

        viewModelScope.launch {
            try {
                deleteConversationUseCase(conversation, currentUserId)
                _event.emit(MessageEvent.ConversationDeleted)
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update { it.copy(loading = false) }
            }
        }
    }

    private fun seeMessages() {
        viewModelScope.launch {
            user?.let {
                messageRepository.updateSeenMessages(conversation.id, it.id)
            }
        }
    }

    private fun emitNewMessageReceived() {
        viewModelScope.launch {
            messageRepository.getLastMessageFlow(conversation.id)
                .filterNotNull()
                .filter { Duration.between(it.date, LocalDateTime.now(ZoneOffset.UTC)).toMinutes() < 1L }
                .collect {
                    _event.emit(MessageEvent.NewMessage(it))
                }
        }
    }

    private fun seeNewMessage() {
        viewModelScope.launch {
            messageRepository.getLastMessageFlow(conversation.id)
                .filterNotNull()
                .filter { it.senderId != user?.id }
                .filter { it.seen.not() }
                .collect {
                    messageRepository.updateSeenMessage(it)
                }
        }
    }

    private fun listenConversation() {
        viewModelScope.launch {
            conversationRepository.getConversationFlow(conversation.interlocutor.id)
                .collect {
                    conversation = it
                }
        }
    }

    private fun clearChatNotifications() {
        viewModelScope.launch {
            messageNotificationManager.clearNotifications(conversation.id)
        }
    }

    private fun listenCurrentUser() {
        viewModelScope.launch {
            userRepository.user.collect { currentUser ->
                _uiState.update { it.copy(currentUser = currentUser) }
            }
        }
    }

    private fun listenBlockUserIds() {
        viewModelScope.launch {
            val interlocutorId = conversation.interlocutor.id
            blockedUserRepository.blockedUserIds.collect { blockedUserIds ->
                _uiState.update {
                    it.copy(userBlocked = blockedUserIds.contains(interlocutorId))
                }
            }
        }
    }

    internal data class ChatUiState(
        val messageText: String,
        val loading: Boolean,
        val userBlocked: Boolean,
        val currentUser: User?
    )

    internal sealed class MessageEvent: SingleUiEvent {
        data class NewMessage(val message: Message): MessageEvent()
        data object MessageReported: MessageEvent()
        data object ConversationDeleted: MessageEvent()
    }
}