package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.message.R
import com.upsaclay.message.domain.conversationsUIFixture
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.domain.entity.ConversationUi
import com.upsaclay.message.domain.mapper.toConversation
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConversationDestination(
    onConversationClick: (Conversation) -> Unit,
    onCreateConversation: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: ConversationViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))

                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    ConversationScreen(
        conversations = uiState.conversations,
        loading = uiState.loading,
        onConversationClick = onConversationClick,
        onDeleteConversationClick = viewModel::deleteConversation,
        onCreateConversationClick = onCreateConversation,
        onRecreateConversationClick = viewModel::recreateConversation,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    )
}

@Composable
private fun ConversationScreen(
    conversations: List<ConversationUi>?,
    loading: Boolean,
    onConversationClick: (Conversation) -> Unit,
    onDeleteConversationClick: (Conversation) -> Unit,
    onCreateConversationClick: () -> Unit,
    onRecreateConversationClick: (Conversation) -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    bottomBar: @Composable () -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<ConversationScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<ConversationScreenDialog?>(null) }

    when(val dialog = activeDialog) {
        is ConversationScreenDialog.DeleteConversationDialog -> {
            DefaultDialog(
                title = stringResource(id = R.string.delete_conversation_dialog_title),
                text = stringResource(id = R.string.delete_conversation_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteConversationClick(dialog.conversation)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    if (loading) {
        LoadingDialog()
    }

    ConversationScaffold(
        onCreateConversation = onCreateConversationClick,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    ) { paddingValues ->
        conversations?.let { conversations ->
            ConversationFeed(
                modifier = Modifier.padding(paddingValues),
                conversationsUi = conversations,
                onClick = {
                    if (it.state == Conversation.ConversationState.CREATED) {
                        onConversationClick(it.toConversation())
                    } else {
                        activeBottomSheet = ConversationScreenBottomSheet.ConversationBottomSheet(it.toConversation())
                    }
                },
                onLongClick = {
                    activeBottomSheet = ConversationScreenBottomSheet.ConversationBottomSheet(it.toConversation())
                },
                onCreateClick = onCreateConversationClick
            )
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar(
                    modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                )
            }
        }
    }

    when(val bottomSheetType = activeBottomSheet)  {
        is ConversationScreenBottomSheet.ConversationBottomSheet -> {
            ConversationBottomSheet(
                conversationState = bottomSheetType.conversation.state,
                onRecreateClick = {
                    onRecreateConversationClick(bottomSheetType.conversation)
                },
                onDeleteClick = {
                    activeBottomSheet = null
                    activeDialog = ConversationScreenDialog.DeleteConversationDialog(bottomSheetType.conversation)
                },
                onDismiss = { activeBottomSheet = null }
            )
        }

        else -> Unit
    }
}

private sealed class ConversationScreenBottomSheet {
    data class ConversationBottomSheet(val conversation: Conversation): ConversationScreenBottomSheet()
}

private sealed class ConversationScreenDialog {
    data class DeleteConversationDialog(val conversation: Conversation): ConversationScreenDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun ConversationsScreenPreview() {
    val conversations = conversationsUIFixture.sortedByDescending { it.lastMessage.date }

    GedoiseTheme {
        Surface {
            ConversationScreen(
                conversations = conversations,
                loading = false,
                onConversationClick = {},
                onDeleteConversationClick = {},
                onCreateConversationClick = {},
                onRecreateConversationClick = {},
                bottomBar = {}
            )
        }
    }
}