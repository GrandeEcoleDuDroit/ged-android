package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.presentation.components.SimpleFloatingActionButton

@Composable
fun NewsScaffold(
    user: User,
    onCreateAnnouncementClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { NewsTopBar() },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        floatingActionButton = {
            if (user.isMember) {
                SimpleFloatingActionButton(
                    modifier = Modifier
                        .testTag(stringResource(id = com.upsaclay.news.R.string.news_screen_create_announcement_button_tag)),
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            stringResource(id = com.upsaclay.news.R.string.new_announcement)
                        )
                    },
                    onClick = onCreateAnnouncementClick
                )
            }
        },
        content = content
    )
}