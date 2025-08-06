package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.upsaclay.common.domain.entity.User
import com.upsaclay.news.presentation.announcement.components.CreateAnnouncementFAB

@Composable
fun NewsScaffold(
    user: User?,
    onCreateAnnouncementClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            NewsTopBar(
                userProfilePictureUrl = user?.profilePictureUrl,
                onProfilePictureClick = onProfilePictureClick
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        floatingActionButton = {
            if (user?.isMember == true) {
                CreateAnnouncementFAB(
                    onClick = onCreateAnnouncementClick
                )
            }
        },
        content = content
    )
}