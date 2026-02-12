package com.upsaclay.news.presentation.news.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun NewsScaffold(
    user: User,
    onCreateAnnouncementClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(onTap = { expanded = false })
            },
        topBar = {
            NewsTopBar(
                admin = user.admin,
                expanded = expanded,
                onOpenMenuButtonClick = { expanded = true },
                onDismissMenu = { expanded = false },
                onCreateAnnouncementClick = onCreateAnnouncementClick,
                onCreatePostClick = onCreatePostClick
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        },
        content = content
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun NewsScaffoldPreview() {
    GedoiseTheme {
        NewsScaffold(
            user = userFixture,
            onCreateAnnouncementClick = {},
            onCreatePostClick = {},
            snackbarHostState = SnackbarHostState(),
            bottomBar = {},
            content = {}
        )
    }
}

@Preview
@Composable
fun SmartExpandableText() {
    GedoiseTheme {
        OutlinedButton({}) {
            Text("YES")
        }
    }
}