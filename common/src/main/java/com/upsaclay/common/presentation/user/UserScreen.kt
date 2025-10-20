package com.upsaclay.common.presentation.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun UserDestination(
    onBackClick: () -> Unit,
    user: User,
    viewModel: UserViewModel = koinViewModel(
        parameters = { parametersOf(user.id) }
    )
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
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    if (uiState.currentUser != null) {
        UserScreen(
            onBackClick = onBackClick,
            onReportUserClick = viewModel::reportUser,
            onBlockUserClick = viewModel::blockUser,
            onUnblockUserClick = viewModel::unblockUser,
            user = user,
            currentUser = uiState.currentUser!!,
            loading = uiState.loading,
            userBlocked = uiState.userBlocked,
            snackbarHostState = snackbarHostState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreen(
    onBackClick: () -> Unit,
    onReportUserClick: (UserReport) -> Unit,
    onBlockUserClick: (String) -> Unit,
    onUnblockUserClick: (String) -> Unit,
    user: User,
    currentUser: User,
    loading: Boolean,
    userBlocked: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    var showUserBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }
    var showBlockUserDialog by remember { mutableStateOf(false) }
    var showUnblockUserDialog by remember { mutableStateOf(false) }
    val userName = if (!user.isDeleted) user.fullName else stringResource(id = R.string.deleted_user)

    if (loading) {
        LoadingDialog()
    }

    if (showBlockUserDialog) {
        DefaultDialog(
            title = stringResource(id = R.string.block_user_dialog_title),
            text = stringResource(id = R.string.block_user_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.block),
            critical = true,
            onConfirm = {
                showBlockUserDialog = false
                onBlockUserClick(user.id)
            },
            onCancel = { showBlockUserDialog = false }
        )
    }

    if (showUnblockUserDialog) {
        DefaultDialog(
            text = stringResource(id = R.string.unblock_user_dialog_message),
            confirmText = stringResource(id = R.string.unblock),
            onConfirm = {
                showUnblockUserDialog = false
                onUnblockUserClick(user.id)
            },
            onCancel = { showUnblockUserDialog = false }
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = userName,
                leadingIcon = {
                    if (user != currentUser) {
                        OptionButton(
                            onClick = { showUserBottomSheet = true },
                            contentDescription = "Show user options"
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .mediumPadding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = 1.8f
            )

            if (!user.isDeleted) {
                SelectionContainer {
                    UserInformationItems(user = user)
                }
            }
        }
    }

    if (showUserBottomSheet) {
        UserBottomSheet(
            onDismiss = { showUserBottomSheet = false },
            onReportClick = {
                showUserBottomSheet = false
                showReportBottomSheet = true
            },
            onBlockClick = {
                showUserBottomSheet = false
                showBlockUserDialog = true
            },
            onUnblockClick = {
                showUserBottomSheet = false
                showUnblockUserDialog = true
            },
            isBlocked = userBlocked
        )
    }

    if (showReportBottomSheet) {
        ReportBottomSheet(
            items = UserReport.Reason.entries,
            onDismiss = { showReportBottomSheet = false },
            onReportClick = { reason ->
                showReportBottomSheet = false
                onReportUserClick(
                    UserReport(
                        userId = user.id,
                        userInfo = UserReport.UserInfo(
                            fullName = user.fullName,
                            email = user.email
                        ),
                        reporterInfo = UserReport.UserInfo(
                            fullName = currentUser.fullName,
                            email = currentUser.email
                        ),
                        reason = reason
                    )
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserBottomSheet(
    onDismiss: () -> Unit,
    onReportClick: () -> Unit,
    onBlockClick: () -> Unit,
    onUnblockClick: () -> Unit,
    isBlocked: Boolean
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        if (isBlocked) {
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.unblock)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.ic_outline_block),
                        contentDescription = null
                    )
                },
                onClick = onUnblockClick
            )
        } else {
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.block)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = com.upsaclay.common.R.drawable.ic_outline_block),
                        contentDescription = null
                    )
                },
                onClick = onBlockClick
            )
        }

        TextItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.report),
                    color = MaterialTheme.colorScheme.error
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ic_outline_report),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = onReportClick
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun UserScreenPreview() {
    GedoiseTheme {
        Surface {
            UserScreen(
                onBackClick = {},
                onReportUserClick = {},
                onBlockUserClick = {},
                onUnblockUserClick = {},
                user = userFixture,
                currentUser = userFixture2,
                loading = false,
                userBlocked = false
            )
        }
    }
}