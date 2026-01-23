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
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.displayName
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
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
    var activeBottomSheet by remember { mutableStateOf<UserScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<UserScreenDialog?>(null) }

    when(activeDialog) {
        is UserScreenDialog.BlockUserDialog -> {
            DefaultDialog(
                title = stringResource(id = R.string.block_user_dialog_title),
                text = stringResource(id = R.string.block_user_dialog_message),
                confirmText = stringResource(id = R.string.block),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onBlockUserClick(user.id)
                },
                onCancel = { activeDialog = null }
            )
        }

        is UserScreenDialog.UnblockUserDialog -> {
            DefaultDialog(
                text = stringResource(id = R.string.unblock_user_dialog_message),
                confirmText = stringResource(id = R.string.unblock),
                onConfirm = {
                    activeDialog = null
                    onUnblockUserClick(user.id)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = user.displayName(),
                leadingIcon = {
                    if (user != currentUser) {
                        OptionButton(
                            onClick = { activeBottomSheet = UserScreenBottomSheet.UserBottomSheet },
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
                .rootMediumPadding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.mediumSpacing()
        ) {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = 1.8f
            )

            if (user.state != User.UserState.DELETED) {
                SelectionContainer {
                    UserInformationItems(user = user)
                }
            }
        }
    }

    when(activeBottomSheet) {
        is UserScreenBottomSheet.UserBottomSheet -> {
            UserBottomSheet(
                onBlockClick = {
                    activeBottomSheet = null
                    activeDialog = UserScreenDialog.BlockUserDialog
                },
                onUnblockClick = {
                    activeBottomSheet = null
                    activeDialog = UserScreenDialog.UnblockUserDialog
                },
                onReportClick = { activeBottomSheet = UserScreenBottomSheet.UserReportBottomSheet },
                isBlocked = userBlocked,
                onDismiss = { activeBottomSheet = null }
            )
        }

        is UserScreenBottomSheet.UserReportBottomSheet -> {
            ReportBottomSheet(
                items = UserReport.Reason.entries,
                onReportClick = { reason ->
                    activeBottomSheet = null
                    onReportUserClick(
                        UserReport(
                            reportedUser = UserReport.ReportedUser(
                                id = user.id,
                                fullName = user.fullName,
                                email = user.email
                            ),
                            reporter = UserReport.Reporter(
                                fullName = currentUser.fullName,
                                email = currentUser.email
                            ),
                            reason = reason
                        )
                    )
                },
                onDismiss = { activeBottomSheet = null },
            )
        }

        else -> Unit
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
                        text = stringResource(id = R.string.unblock)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_block),
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
                        text = stringResource(id = R.string.block)
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_block),
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
                    text = stringResource(id = R.string.report),
                    color = MaterialTheme.colorScheme.error
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_outline_report),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = onReportClick
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_padding)))
    }
}

private sealed class UserScreenBottomSheet {
    data object UserBottomSheet: UserScreenBottomSheet()
    data object UserReportBottomSheet: UserScreenBottomSheet()
}

private sealed class UserScreenDialog {
    data object BlockUserDialog: UserScreenDialog()
    data object UnblockUserDialog: UserScreenDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
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