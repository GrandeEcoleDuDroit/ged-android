package com.upsaclay.gedoise.presentation.profile.blockedusers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlockedUsersDestination(
    onBackClick: () -> Unit,
    onAccountClick: (User) -> Unit,
    viewModel: BlockedUsersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    BlockedUsersScreen(
        onBackClick = onBackClick,
        blockedUsers = uiState.blockedUsers,
        snackbarHostState = snackbarHostState,
        onUnblockClick = viewModel::unblockUser,
        onAccountClick = onAccountClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockedUsersScreen(
    onBackClick: () -> Unit,
    blockedUsers: List<User>,
    snackbarHostState: SnackbarHostState,
    onUnblockClick: (String) -> Unit,
    onAccountClick: (User) -> Unit
) {
    var showUnblockUserDialog by remember { mutableStateOf(false) }
    var clickedUserId by remember { mutableStateOf<String?>(null) }

    if (showUnblockUserDialog) {
        DefaultDialog(
            text = stringResource(com.upsaclay.common.R.string.unblock_user_dialog_message),
            confirmText = stringResource(com.upsaclay.common.R.string.unblock),
            onConfirm = {
                showUnblockUserDialog = false
                clickedUserId?.let(onUnblockClick)
            },
            onCancel = { showUnblockUserDialog = false },
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.blocked_users)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (blockedUsers.isEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier
                            .height(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
                            .testTag(stringResource(R.string.empty_blocked_users_list_tag))
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_blocked_user),
                        color = MaterialTheme.colorScheme.informationText,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(blockedUsers) { user ->
                    UserItem(
                        modifier = Modifier
                            .clickable(onClick = { onAccountClick(user) })
                            .testTag(stringResource(R.string.blocked_user_item_tag) + user.id),
                        user = user,
                        trailingContent = {
                            TextButton(
                                onClick = {
                                    clickedUserId = user.id
                                    showUnblockUserDialog = true
                                }
                            ) {
                                Text(text = stringResource(com.upsaclay.common.R.string.unblock))
                            }
                        }
                    )
                }
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun BlockedUserScreenPreview() {
    GedoiseTheme {
        Surface {
            BlockedUsersScreen(
                onBackClick = {},
                blockedUsers = usersFixture,
                snackbarHostState = SnackbarHostState(),
                onUnblockClick = {},
                onAccountClick = {}
            )
        }
    }
}