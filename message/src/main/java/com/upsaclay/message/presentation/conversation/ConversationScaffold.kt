package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.message.R

@Composable
fun ConversationScaffold(
    onCreateConversation: () -> Unit,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        topBar = {
            TitleTopBar(
                title = stringResource(com.upsaclay.common.R.string.messages),
                actions = {
                    IconButton(onClick = onCreateConversation) {
                        Icon(
                            painter = painterResource(com.upsaclay.common.R.drawable.ic_add),
                            contentDescription = stringResource(id = R.string.create_conversation_icon_button_description)
                        )
                    }
                }
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
        },
        content = content
    )
}