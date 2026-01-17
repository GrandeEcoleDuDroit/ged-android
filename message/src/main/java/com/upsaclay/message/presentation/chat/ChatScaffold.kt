package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun ChatScaffold(
    modifier: Modifier = Modifier,
    interlocutor: User,
    snackbarHostState: SnackbarHostState,
    bottomBar: @Composable () -> Unit = {},
    onBackClick: () -> Unit,
    onInterlocutorClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ChatTopBar(
                interlocutor = interlocutor,
                onBackClick = onBackClick,
                onInterlocutorClick = onInterlocutorClick
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
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
private fun ChatScaffoldPreview() {
    GedoiseTheme {
        Surface {
            ChatScaffold(
                interlocutor = userFixture,
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                onInterlocutorClick = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentAlignment = Alignment.Center,
                    content = {}
                )
            }
        }
    }
}