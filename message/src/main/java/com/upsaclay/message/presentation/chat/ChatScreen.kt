package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.Message
import com.upsaclay.message.domain.entity.MessageReport
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

                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    ChatScreen(
        conversation = conversation,
        messages = viewModel.messages,
        text = uiState.text,
        snackbarHostState = snackbarHostState,
        newMessageEvent = newMessageEvent,
        onBackClick = onBackClick,
        onTextChange = viewModel::onTextChange,
        onSendMessage = viewModel::sendMessage,
        onResendMessageClick = viewModel::resendMessage,
        onDeleteMessageClick = viewModel::deleteMessage,
        onReportClick = viewModel::reportMessage
    )
}

@Composable
private fun ChatScreen(
    conversation: Conversation,
    messages: Flow<PagingData<Message>>,
    text: String,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    newMessageEvent: MessageEvent.NewMessage?,
    onBackClick: () -> Unit,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onResendMessageClick: (Message) -> Unit,
    onDeleteMessageClick: (Message) -> Unit,
    onReportClick: (MessageReport) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var messageClicked: Message? by remember { mutableStateOf(null) }
    var showDeleteMessageDialog by remember { mutableStateOf(false) }
    var showSentMessageBottomSheet by remember { mutableStateOf(false) }
    var showReceivedMessageBottomSheet by remember { mutableStateOf(false) }
    var showReportMessageBottomSheet by remember { mutableStateOf(false) }

    if (showDeleteMessageDialog) {
        SensibleActionDialog(
            title = stringResource(id = R.string.delete_message_dialog_title),
            text = stringResource(id = R.string.delete_message_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteMessageDialog = false
                messageClicked?.let(onDeleteMessageClick)
            },
            onCancel = { showDeleteMessageDialog  = false }
        )
    }

    Scaffold(
        topBar = {
            ChatTopBar(
                interlocutor = conversation.interlocutor,
                onBackClick = {
                    keyboardController?.hide()
                    onBackClick()
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .height(64.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                MessageInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = onTextChange,
                    onSendClick = onSendMessage
                )
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) {
                Snackbar(it)
            }
        },
    ) { paddingValues ->
        MessageFeed(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            messages = messages,
            interlocutor = conversation.interlocutor,
            newMessageEvent = newMessageEvent,
            onErrorSentMessageClick = {
                messageClicked = it
                showSentMessageBottomSheet = true
            },
            onReceivedMessageLongClick = {
                messageClicked = it
                showReceivedMessageBottomSheet = true
            }
        )
    }

    if (showSentMessageBottomSheet) {
        SentMessageBottomSheet(
            onDismiss = { showSentMessageBottomSheet = false },
            onResendMessageClick = {
                showSentMessageBottomSheet = false
                messageClicked?.let(onResendMessageClick)
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
                messageClicked?.let {
                    showReportMessageBottomSheet = true
                }
            }
        )
    }

    if (showReportMessageBottomSheet) {
        ReportMessageBottomSheet(
            onDismiss = { showReportMessageBottomSheet = false },
            onReasonClick = { reason ->
                showReportMessageBottomSheet = false
                messageClicked?.let { message ->
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
            text = text,
            newMessageEvent = null,
            onBackClick = {},
            onTextChange = { text = it },
            onSendMessage = {},
            onDeleteMessageClick = {},
            onResendMessageClick = {},
            onReportClick = {}
        )
    }
}