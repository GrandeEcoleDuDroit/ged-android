package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.Message.MessageState
import com.upsaclay.message.domain.entity.MessageReport
import com.upsaclay.message.domain.fixtures.conversationFixture
import com.upsaclay.message.domain.fixtures.messagesFixture
import com.upsaclay.message.presentation.chat.ChatViewModel.MessageEvent
import com.upsaclay.message.presentation.stringRes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChatDestination(
    conversation: Conversation,
    onBackClick: () -> Unit,
    onInterlocutorClick: (User) -> Unit,
    viewModel: ChatViewModel = koinViewModel {
        parametersOf(conversation)
    }
) {
    val uiState by viewModel.uiState.collectAsState()
    var newMessageEvent by remember { mutableStateOf<MessageEvent.NewMessage?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is MessageEvent.NewMessage -> newMessageEvent = event
                is MessageEvent.MessageReported -> showSnackBar(context.getString(R.string.message_reported))
                is MessageEvent.ChatDeleted -> onBackClick()
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    LifecycleResumeEffect(Unit) {
        viewModel.startSeeingMessages()

        onPauseOrDispose {
            viewModel.stopSeeingMessages()
        }
    }

    ChatScreen(
        conversation = conversation,
        messages = viewModel.messages,
        messageText = uiState.messageText,
        loading = uiState.loading,
        isUserBlocked = uiState.isUserBlocked,
        snackbarHostState = snackbarHostState,
        newMessageEvent = newMessageEvent,
        onBackClick = onBackClick,
        onInterlocutorClick = onInterlocutorClick,
        onMessageTextChange = viewModel::onMessageTextChange,
        onSendMessageClick = viewModel::sendMessage,
        onResendMessageClick = viewModel::resendErrorMessage,
        onDeleteMessageClick = viewModel::deleteErrorMessage,
        onReportClick = viewModel::reportMessage,
        onUnblockUserClick = viewModel::unblockUser,
        onDeleteConversationClick = viewModel::deleteChat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    conversation: Conversation,
    messages: Flow<PagingData<Message>>,
    messageText: String,
    loading: Boolean,
    isUserBlocked: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    newMessageEvent: MessageEvent.NewMessage?,
    onBackClick: () -> Unit,
    onInterlocutorClick: (User) -> Unit,
    onMessageTextChange: (String) -> Unit,
    onSendMessageClick: () -> Unit,
    onResendMessageClick: (Message) -> Unit,
    onDeleteMessageClick: (Message) -> Unit,
    onReportClick: (MessageReport) -> Unit,
    onUnblockUserClick: (String) -> Unit,
    onDeleteConversationClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var activeBottomSheet by remember { mutableStateOf<ChatScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<ChatDialog?>(null) }

    when(val dialog = activeDialog) {
        is ChatDialog.DeleteMessageDialog -> {
            DefaultDialog(
                text = stringResource(id = R.string.delete_message_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteMessageClick(dialog.message)
                },
                onCancel = { activeDialog = null }
            )
        }

        is ChatDialog.DeleteConversationDialog -> {
            DefaultDialog(
                title = stringResource(id = R.string.delete_conversation_dialog_title),
                text = stringResource(id = R.string.delete_conversation_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteConversationClick()
                },
                onCancel = { activeDialog = null }
            )
        }

        is ChatDialog.UnblockUserDialog -> {
            DefaultDialog(
                text = stringResource(id = com.upsaclay.common.R.string.unblock_user_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.unblock),
                onConfirm = {
                    activeDialog = null
                    onUnblockUserClick(conversation.interlocutor.id)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    if (loading) {
        LoadingDialog()
    }

    ChatScaffold(
        modifier = Modifier.imePadding(),
        interlocutor = conversation.interlocutor,
        snackbarHostState = snackbarHostState,
        onBackClick = {
            focusManager.clearFocus()
            onBackClick()
        },
        onInterlocutorClick = { onInterlocutorClick(conversation.interlocutor) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .rootMediumPadding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.smallMediumSpacing()
        ) {
            MessageFeed(
                modifier = Modifier.weight(1f),
                messages = messages,
                interlocutor = conversation.interlocutor,
                newMessageEvent = newMessageEvent,
                onErrorSentMessageClick = {
                    if (it.state == MessageState.ERROR) {
                        activeBottomSheet = ChatScreenBottomSheet.SentMessageBottomSheet(it)
                    }
                },
                onReceivedMessageLongClick = {
                    activeBottomSheet = ChatScreenBottomSheet.ReceivedMessageBottomSheet(it)
                },
                onInterlocutorClick = { onInterlocutorClick(conversation.interlocutor) }
            )

            MessageBottomSection(
                modifier = Modifier.fillMaxWidth(),
                isUserBlocked = isUserBlocked,
                messageText = messageText,
                onMessageTextChange = onMessageTextChange,
                onSendMessageClick = onSendMessageClick,
                onDeleteConversationClick = { activeDialog = ChatDialog.DeleteConversationDialog },
                onUnblockUserClick = { activeDialog = ChatDialog.UnblockUserDialog }
            )
        }
    }

    when(val bottomSheet = activeBottomSheet) {
        is ChatScreenBottomSheet.SentMessageBottomSheet -> {
            SentMessageBottomSheet(
                onResendMessageClick = {
                    activeBottomSheet = null
                    onResendMessageClick(bottomSheet.message)
                },
                onDeleteMessageClick = {
                    activeBottomSheet = null
                    activeDialog = ChatDialog.DeleteMessageDialog(bottomSheet.message)
                },
                onDismiss = { activeBottomSheet = null },
            )
        }

        is ChatScreenBottomSheet.ReceivedMessageBottomSheet -> {
            ReceivedMessageBottomSheet(
                onReportClick = {
                    activeBottomSheet = ChatScreenBottomSheet.MessageReportBottomSheet(bottomSheet.message)
                },
                onDismiss = { activeBottomSheet = null }
            )
        }

        is ChatScreenBottomSheet.MessageReportBottomSheet -> {
            ReportBottomSheet(
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                items = MessageReport.Reason.entries.map { stringResource(it.stringRes) },
                onReportClick = { reason ->
                    activeBottomSheet = null
                    onReportClick(
                        MessageReport(
                            conversationId = conversation.id,
                            messageId = bottomSheet.message.id,
                            recipient = MessageReport.Recipient(
                                fullName = conversation.interlocutor.fullName,
                                email = conversation.interlocutor.email
                            ),
                            reason = reason
                        )
                    )
                },
                onDismiss = { activeBottomSheet = null },
            )
        }

        null -> Unit
    }
}

@Composable
private fun MessageBottomSection(
    modifier: Modifier = Modifier,
    isUserBlocked: Boolean,
    messageText: String,
    onMessageTextChange: (String) -> Unit,
    onSendMessageClick: () -> Unit,
    onDeleteConversationClick: () -> Unit,
    onUnblockUserClick: () -> Unit
) {
    if (isUserBlocked) {
        MessageBlockedUserIndicator(
            modifier = modifier.testTag(stringResource(R.string.chat_screen_blocked_user_indicator_tag)),
            onDeleteChatClick = onDeleteConversationClick,
            onUnblockUserClick = onUnblockUserClick
        )
    } else {
        MessageInput(
            modifier = modifier.testTag(stringResource(R.string.chat_screen_message_input_tag)),
            value = messageText,
            onValueChange = onMessageTextChange,
            onSendClick = onSendMessageClick
        )
    }
}

private sealed class ChatScreenBottomSheet {
    data class SentMessageBottomSheet(val message: Message): ChatScreenBottomSheet()
    data class ReceivedMessageBottomSheet(val message: Message): ChatScreenBottomSheet()
    data class MessageReportBottomSheet(val message: Message): ChatScreenBottomSheet()
}

private sealed class ChatDialog {
    data class DeleteMessageDialog(val message: Message): ChatDialog()
    data object DeleteConversationDialog: ChatDialog()
    data object UnblockUserDialog: ChatDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ChatScreenPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        ChatScreen(
            conversation = conversationFixture,
            messages = flowOf(PagingData.from(messagesFixture)),
            messageText = text,
            loading = false,
            isUserBlocked = false,
            newMessageEvent = null,
            onBackClick = {},
            onInterlocutorClick = {},
            onMessageTextChange = { text = it },
            onSendMessageClick = {},
            onDeleteMessageClick = {},
            onResendMessageClick = {},
            onReportClick = {},
            onUnblockUserClick = {},
            onDeleteConversationClick = {}
        )
    }
}