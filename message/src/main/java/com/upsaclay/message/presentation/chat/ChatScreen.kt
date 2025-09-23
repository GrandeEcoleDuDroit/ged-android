package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
import com.upsaclay.message.domain.entity.MessageState
import com.upsaclay.message.domain.messagesFixture
import com.upsaclay.message.presentation.chat.ChatViewModel.MessageEvent
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

    ChatScreen(
        conversation = conversation,
        messages = viewModel.messages,
        messageText = uiState.messageText,
        loading = uiState.loading,
        userBlocked = uiState.isUserBlocked,
        snackbarHostState = snackbarHostState,
        newMessageEvent = newMessageEvent,
        onBackClick = onBackClick,
        onInterlocutorClick = onInterlocutorClick,
        onMessageTextChange = viewModel::onMessageTextChange,
        onSendMessage = viewModel::sendMessage,
        onResendMessageClick = viewModel::resendMessage,
        onDeleteMessageClick = viewModel::deleteMessage,
        onReportClick = viewModel::reportMessage,
        onUnblockUser = viewModel::unblockUser,
        onDeleteChat = viewModel::deleteChat
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    conversation: Conversation,
    messages: Flow<PagingData<Message>>,
    messageText: String,
    loading: Boolean,
    userBlocked: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    newMessageEvent: MessageEvent.NewMessage?,
    onBackClick: () -> Unit,
    onInterlocutorClick: (User) -> Unit,
    onMessageTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onResendMessageClick: (Message) -> Unit,
    onDeleteMessageClick: (Message) -> Unit,
    onReportClick: (MessageReport) -> Unit,
    onUnblockUser: (String) -> Unit,
    onDeleteChat: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var clickedMessage: Message? by remember { mutableStateOf(null) }
    var showDeleteMessageDialog by remember { mutableStateOf(false) }
    var showSentMessageBottomSheet by remember { mutableStateOf(false) }
    var showReceivedMessageBottomSheet by remember { mutableStateOf(false) }
    var showReportMessageBottomSheet by remember { mutableStateOf(false) }
    var showDeleteChatDialog by remember { mutableStateOf(false) }

    if (showDeleteMessageDialog) {
        SensibleActionDialog(
            title = stringResource(id = R.string.delete_message_dialog_title),
            text = stringResource(id = R.string.delete_message_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteMessageDialog = false
                clickedMessage?.let(onDeleteMessageClick)
            },
            onCancel = { showDeleteMessageDialog  = false }
        )
    }

    if (showDeleteChatDialog) {
        SensibleActionDialog(
            title = stringResource(id = R.string.delete_chat_dialog_title),
            text = stringResource(id = R.string.delete_chat_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteChatDialog = false
                onDeleteChat()
            },
            onCancel = { showDeleteChatDialog  = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    ChatScaffold(
        modifier = Modifier.imePadding(),
        interlocutor = conversation.interlocutor,
        userBlocked = userBlocked,
        messageText = messageText,
        snackbarHostState = snackbarHostState,
        onTextChange = onMessageTextChange,
        onSendMessage = onSendMessage,
        onBackClick = {
            keyboardController?.hide()
            onBackClick()
        },
        onInterlocutorClick = { onInterlocutorClick(conversation.interlocutor) },
        onDeleteChatClick = { showDeleteChatDialog = true  },
        onUnblockUserClick = { onUnblockUser(conversation.interlocutor.id) }
    ) { innerPadding ->
        MessageFeed(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            messages = messages,
            interlocutor = conversation.interlocutor,
            newMessageEvent = newMessageEvent,
            onErrorSentMessageClick = {
                if (it.state == MessageState.ERROR) {
                    clickedMessage = it
                    showSentMessageBottomSheet = true
                }
            },
            onReceivedMessageLongClick = {
                clickedMessage = it
                showReceivedMessageBottomSheet = true
            }
        )
    }

    if (showSentMessageBottomSheet) {
        SentMessageBottomSheet(
            onDismiss = { showSentMessageBottomSheet = false },
            onResendMessageClick = {
                showSentMessageBottomSheet = false
                clickedMessage?.let(onResendMessageClick)
            },
            onDeleteMessageClick = {
                showSentMessageBottomSheet = false
                showDeleteMessageDialog = true
            }
        )
    }

    if (showReceivedMessageBottomSheet) {
        ReceivedMessageBottomSheet(
            onDismiss = { showReceivedMessageBottomSheet = false },
            onReportClick = {
                showReceivedMessageBottomSheet = false
                showReportMessageBottomSheet = true
            }
        )
    }

    if (showReportMessageBottomSheet) {
        ReportBottomSheet(
            sheetState = bottomSheetState,
            items = MessageReport.Reason.entries,
            onDismiss = { showReportMessageBottomSheet = false },
            onReportClick = { reason ->
                showReportMessageBottomSheet = false
                clickedMessage?.let { message ->
                    onReportClick(
                        MessageReport(
                            conversationId = conversation.id,
                            messageId = message.id,
                            recipientInfo = MessageReport.UserInfo(
                                fullName = conversation.interlocutor.fullName,
                                email = conversation.interlocutor.email
                            ),
                            reason = reason
                        )
                    )
                }
            }
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun ChatScreenPreview() {
    var text by remember { mutableStateOf("") }

    GedoiseTheme {
        ChatScreen(
            conversation = conversationFixture,
            messages = flowOf(PagingData.from(messagesFixture)),
            messageText = text,
            loading = false,
            userBlocked = true,
            newMessageEvent = null,
            onBackClick = {},
            onInterlocutorClick = {},
            onMessageTextChange = { text = it },
            onSendMessage = {},
            onDeleteMessageClick = {},
            onResendMessageClick = {},
            onReportClick = {},
            onUnblockUser = {},
            onDeleteChat = {}
        )
    }
}