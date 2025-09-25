package com.upsaclay.gedoise.presentation.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileDestination(
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.user != null) {
        ProfileScreen(
            user = uiState.user!!,
            onBackClick = onBackClick,
            onLogoutClick = viewModel::logout,
            onAccountClick = onAccountClick,
            onPrivacyClick = onPrivacyClick
        )
    } else {
        CircularProgressBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { showLogoutDialog = false }
    }

    if (showLogoutDialog) {
        SensibleActionDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.profile_screen_logout_dialog_tag)),
            title = stringResource(id = R.string.logout),
            text = stringResource(id = R.string.logout_dialog_message),
            cancelText = stringResource(id = com.upsaclay.common.R.string.cancel),
            confirmText = stringResource(id = R.string.logout),
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onCancel = { showLogoutDialog = false }
        )
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = com.upsaclay.common.R.string.profile)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column {
                TopSection(
                    profilePictureUrl = user.profilePictureUrl,
                    userFullName = user.fullName
                )

                HorizontalDivider()

                TextItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(id = R.string.profile_screen_account_info_button_tag)),
                    text = { Text(text = stringResource(id = R.string.account_informations)) },
                    icon = {
                        Icon(
                            painter = painterResource(id = com.upsaclay.common.R.drawable.ic_fill_person),
                            contentDescription = stringResource(id = R.string.account_icon_description)
                        )
                    },
                    onClick = onAccountClick
                )

                TextItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = stringResource(id = R.string.privacy)) },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_outline_lock),
                            contentDescription = null
                        )
                    },
                    onClick = onPrivacyClick
                )

                TextItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(stringResource(id = R.string.profile_screen_logout_button_tag)),
                    text = {
                        Text(
                            text = stringResource(id = R.string.logout),
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = com.upsaclay.common.R.drawable.ic_logout),
                            contentDescription = stringResource(id = R.string.logout_icon_description),
                            tint = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = { showLogoutDialog = true }
                )
            }
        }
    }
}

@Composable
private fun TopSection(profilePictureUrl: String?, userFullName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding),
                bottom = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
    ) {
        ProfilePicture(
            url = profilePictureUrl,
            scale = 0.7f
        )

        Text(
            text = userFullName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
fun ProfileScreenPreview() {
    GedoiseTheme {
        Surface {
            ProfileScreen(
                user = userFixture,
                onBackClick = {},
                onLogoutClick = {},
                onAccountClick = {},
                onPrivacyClick = {}
            )
        }
    }
}