package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        topBar = { TitleTopBar(title = stringResource(com.upsaclay.common.R.string.messages)) },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
        },
        floatingActionButton = {
            CreateConversationFAB(
                onClick = onCreateConversation
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}

@Composable
private fun CreateConversationFAB(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_message_add),
            contentDescription = stringResource(id = R.string.fab_button_panel_icon_description)
        )
    }
}
