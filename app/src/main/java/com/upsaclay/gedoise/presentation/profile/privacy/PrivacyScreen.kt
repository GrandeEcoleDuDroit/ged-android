package com.upsaclay.gedoise.presentation.profile.privacy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.gedoise.R

@Composable
fun PrivacyDestination(
    onBackClick: () -> Unit,
    onBlockedUsersClick: () -> Unit
) {
    PrivacyScreen(
        onBackClick = onBackClick,
        onBlockedUsersClick = onBlockedUsersClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrivacyScreen(
    onBackClick: () -> Unit,
    onBlockedUsersClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.privacy)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TextItem(
                text = { Text(text = stringResource(R.string.blocked_users)) },
                onClick = onBlockedUsersClick
            )
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
private fun PrivacyScreenPreview() {
    GedoiseTheme {
        PrivacyScreen(
            onBackClick = {},
            onBlockedUsersClick = {}
        )
    }
}