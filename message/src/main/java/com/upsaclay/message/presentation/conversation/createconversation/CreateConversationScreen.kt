package com.upsaclay.message.presentation.conversation.createconversation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.presentation.theme.supportingText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.message.domain.entity.Conversation
import com.upsaclay.message.presentation.conversation.CreateConversationPreviewParameterData
import com.upsaclay.message.presentation.conversation.CreateConversationPreviewParameterProvider
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateConversationDestination(
    onBackClick: () -> Unit,
    onCreateConversationClick: (Conversation) -> Unit,
    viewModel: CreateConversationViewModel = koinViewModel()
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
        viewModel.event.collect {
            if (it is SingleUiEvent.Error) {
                showSnackBar(context.getString(it.messageId))
            }
        }
    }

    CreateConversationScreen(
        users = uiState.users,
        query = uiState.query,
        snackbarHostState = snackbarHostState,
        onQueryChange = viewModel::onQueryChange,
        onResetQuery = viewModel::resetQuery,
        onUserClick = { user ->
            scope.launch {
                viewModel.getConversation(user)?.let(onCreateConversationClick)
            }
        },
        onBackClick = onBackClick
    )
}

@Composable
fun CreateConversationScreen(
    users: List<User>?,
    query: String,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onQueryChange: (String) -> Unit,
    onResetQuery: () -> Unit,
    onUserClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var search by remember { mutableStateOf(false) }

    BackHandler(enabled = search) {
        search = false
    }

    CreateConversationScaffold(
        search = search,
        query = query,
        onQueryChange = onQueryChange,
        onSearchBackClick = {
            search = false
            onResetQuery()
        },
        onBackClick = {
            focusManager.clearFocus()
            onBackClick()
        },
        onSearchClick = { search = true },
        onClearClick = onResetQuery,
        snackbarHostState = snackbarHostState
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.smallSpacing()
        ) {
            users?.let {
                UsersFeed(
                    users = users,
                    onUserClick = {
                        focusManager.clearFocus()
                        onUserClick(it)
                    }
                )
            } ?: run {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.padding.medium),
                    contentAlignment = Alignment.TopCenter
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun UsersFeed(
    users: List<User>,
    onUserClick: (User) -> Unit
) {
    LazyColumn {
        if (users.isNotEmpty()) {
            items(users) { user ->
                UserItem(
                    modifier = Modifier.clickable(onClick = { onUserClick(user) }),
                    user = user
                )
            }
        } else {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.padding.medium),
                    text = stringResource(id = com.upsaclay.common.R.string.no_user),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.supportingText
                )
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun CreateConversationScreenPreview(
    @PreviewParameter(CreateConversationPreviewParameterProvider::class) previewParameter: CreateConversationPreviewParameterData
) {
    var query by remember { mutableStateOf("") }

    GedoiseTheme {
        CreateConversationScreen(
            users = previewParameter.users,
            query = query,
            onQueryChange = { query = it },
            onResetQuery = { query = "" },
            onUserClick = {},
            onBackClick = {}
        )
    }
}