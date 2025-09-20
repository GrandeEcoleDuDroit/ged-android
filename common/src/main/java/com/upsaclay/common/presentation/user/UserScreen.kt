package com.upsaclay.common.presentation.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.R
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.PrimaryButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDestination(
    onBackClick: () -> Unit,
    user: User,
    viewModel: UserViewModel = koinViewModel()
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

    if (uiState.user != null) {
        UserScreen(
            onBackClick = onBackClick,
            onReportUserClick = viewModel::reportUser,
            onBlockUserClick = viewModel::blockUser,
            onUnblockUserClick = viewModel::unblockUser,
            user = user,
            currentUser = uiState.user!!,
            loading = uiState.loading,
            isBlocked = uiState.isBlocked,
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
    isBlocked: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState()
) {
    var showUserBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = user.fullName,
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfilePicture(
                url = user.profilePictureUrl,
                scale = 1.8f
            )

            if (isBlocked) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_padding)))
                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onUnblockUserClick(user.id) },
                    text = stringResource(id = R.string.unblock)
                )
            } else {
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
                onBlockUserClick(user.id)
            },
            onUnblockClick = {
                showUserBottomSheet = false
                onUnblockUserClick(user.id)
            },
            isBlocked = isBlocked
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
            ClickableItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.unblock),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = onBlockClick
            )
        } else {
            ClickableItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.block),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                onClick = onBlockClick
            )
        }

        ClickableItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.report),
                    color = MaterialTheme.colorScheme.error
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
                isBlocked = true
            )
        }
    }
}