package com.upsaclay.common.presentation.user

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.entity.UserReport
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.mediumPadding
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import org.koin.androidx.compose.koinViewModel

@Composable
fun UserDestination(
    onBackClick: () -> Unit,
    user: User,
    viewModel: UserViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.user != null) {
        UserScreen(
            onBackClick = onBackClick,
            onReportClick = viewModel::reportUser,
            user = user,
            currentUser = uiState.user!!
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreen(
    onBackClick: () -> Unit,
    onReportClick: (UserReport) -> Unit,
    user: User,
    currentUser: User
) {
    var showUserBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }

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

            SelectionContainer {
                UserInformationItems(user = user)
            }
        }
    }

    if (showUserBottomSheet) {
        UserBottomSheet(
            onDismiss = { showUserBottomSheet = false },
            onReportClick = {
                showUserBottomSheet = false
                showReportBottomSheet = true
            }
        )
    }

    if (showReportBottomSheet) {
        ReportBottomSheet(
            items = UserReport.Reason.entries,
            onDismiss = { showReportBottomSheet = false },
            onReportClick = { reason ->
                showReportBottomSheet = false
                onReportClick(
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
    onReportClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        ClickableItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.report),
                    color = MaterialTheme.colorScheme.error
                )
            },
            icon = {
                Icon(
                    painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_report),
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
                onReportClick = {},
                user = userFixture,
                currentUser = userFixture2
            )
        }
    }
}