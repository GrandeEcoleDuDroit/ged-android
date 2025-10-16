package com.upsaclay.gedoise.presentation.profile.account

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
import com.upsaclay.gedoise.R

@Composable
fun AccountDestination(
    onBackClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    AccountScreen(
        onBackClick = onBackClick,
        onDeleteAccountClick = onDeleteAccountClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccountScreen(
    onBackClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.account)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TextItem(
                text = { Text(text = stringResource(R.string.delete_account)) },
                onClick = onDeleteAccountClick
            )
        }
    }
}