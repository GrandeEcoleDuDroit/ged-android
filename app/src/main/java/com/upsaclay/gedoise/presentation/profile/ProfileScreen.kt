package com.upsaclay.gedoise.presentation.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.smallSpacing
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.ProfilePicture
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.components.TitleTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.gold
import com.upsaclay.common.presentation.theme.informationText
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.gedoise.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileDestination(
    onAccountInformationClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.user != null) {
        ProfileScreen(
            user = uiState.user!!,
            onAccountInformationClick = onAccountInformationClick,
            onAccountClick = onAccountClick,
            onPrivacyClick = onPrivacyClick,
            onLogoutClick = viewModel::logout,
            bottomBar = bottomBar
        )
    } else {
        CircularProgressBar()
    }
}

@Composable
fun ProfileScreen(
    user: User,
    onAccountInformationClick: () -> Unit,
    onAccountClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLogoutClick: () -> Unit,
    bottomBar: @Composable () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        DefaultDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.profile_screen_logout_dialog_tag)),
            text = stringResource(id = R.string.logout_dialog_message),
            confirmText = stringResource(id = R.string.logout),
            critical = true,
            onConfirm = {
                showLogoutDialog = false
                onLogoutClick()
            },
            onCancel = { showLogoutDialog = false }
        )
    }

    Scaffold(
        topBar = {
            TitleTopBar(title = stringResource(com.upsaclay.common.R.string.profile))
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ListItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAccountInformationClick),
                leadingContent = {
                    ProfilePicture(
                        url = user.profilePictureUrl,
                        scale = 0.6f
                    )
                },
                headlineContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.smallSpacing()
                    ) {
                        Text(
                            modifier = Modifier.weight(1f, fill = false),
                            text = user.fullName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp
                            ),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )

                        if (user.admin) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.gold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                supportingContent = {
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.informationText,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            )

            HorizontalDivider()

            TextItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(id = R.string.profile_screen_account_button_tag)),
                text = { Text(text = stringResource(id = R.string.account)) },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_outline_key),
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
fun ProfileScreenPreview() {
    GedoiseTheme {
        ProfileScreen(
            user = userFixture,
            onAccountInformationClick = {},
            onAccountClick = {},
            onPrivacyClick = {},
            onLogoutClick = {},
            bottomBar = {}
        )
    }
}