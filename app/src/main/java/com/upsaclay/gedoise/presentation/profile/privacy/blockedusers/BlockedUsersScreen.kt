package com.upsaclay.gedoise.presentation.profile.privacy.blockedusers

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.SimpleDialog
import com.upsaclay.common.presentation.components.UserItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.Phones
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlockedUsersDestination(
    onBackClick: () -> Unit,
    onAccountClick: (User) -> Unit,
    viewModel: BlockedUsersViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BlockedUsersScreen(
        onBackClick = onBackClick,
        blockedUsers = uiState.blockedUsers,
        onUnblockClick = viewModel::unblockUser,
        onAccountClick = onAccountClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BlockedUsersScreen(
    onBackClick: () -> Unit,
    blockedUsers: List<User>,
    onUnblockClick: (String) -> Unit,
    onAccountClick: (User) -> Unit
) {
    var showUnblockUserDialog by remember { mutableStateOf(false) }
    var clickedUserId by remember { mutableStateOf<String?>(null) }

    if (showUnblockUserDialog) {
        SimpleDialog(
            title = stringResource(R.string.unblock_user_dialog_title),
            text = stringResource(R.string.unblock_user_dialog_message),
            confirmText = stringResource(R.string.unblock),
            onConfirm = {
                showUnblockUserDialog = false
                clickedUserId?.let(onUnblockClick)
            },
            onCancel = { showUnblockUserDialog = false },
            onDismiss = { showUnblockUserDialog = false }
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(com.upsaclay.gedoise.R.string.blocked_users)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            if (blockedUsers.isEmpty()) {
                item {
                    Spacer(
                        modifier = Modifier.height(dimensionResource(R.dimen.small_padding))
                    )
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.no_blocked_users),
                        color = MaterialTheme.colorScheme.informationText,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(blockedUsers) { user ->
                    UserItem(
                        modifier = Modifier.clickable(onClick = { onAccountClick(user) }),
                        user = user,
                        trailingContent = {
                            TextButton(
                                onClick = {
                                    clickedUserId = user.id
                                    showUnblockUserDialog = true
                                }
                            ) {
                                Text(text = stringResource(R.string.unblock))
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
                blockedUsers = emptyList(),
                onUnblockClick = {},
                onAccountClick = {}
            )
        }
    }
}