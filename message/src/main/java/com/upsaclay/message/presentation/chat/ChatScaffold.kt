package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.message.R

@Composable
fun ChatScaffold(
    modifier: Modifier = Modifier,
    interlocutor: User,
    userBlocked: Boolean,
    messageText: String,
    snackbarHostState: SnackbarHostState,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit,
    onBackClick: () -> Unit,
    onInterlocutorClick: () -> Unit,
    onDeleteChatClick: () -> Unit,
    onUnblockUserClick: () -> Unit,
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
        bottomBar = {
            if (userBlocked) {
                MessageBlockedUserIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
                        .fillMaxWidth()
                        .testTag(stringResource(R.string.chat_screen_blocked_user_indicator_tag)),
                    onDeleteChatClick = onDeleteChatClick,
                    onUnblockUserClick = onUnblockUserClick
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding))
                        .fillMaxWidth()
                ) {
                    MessageInput(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(stringResource(R.string.chat_screen_message_input_tag)),
                        value = messageText,
                        onValueChange = onTextChange,
                        onSendClick = onSendMessage
                    )
                }
            }
        },
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

@Phones
@Composable
private fun ChatScaffoldPreview() {
    GedoiseTheme {
        Surface {
            ChatScaffold(
                interlocutor = userFixture,
                userBlocked = false,
                messageText = "",
                snackbarHostState = SnackbarHostState(),
                onTextChange = {},
                onSendMessage = {},
                onBackClick = {},
                onInterlocutorClick = {},
                onDeleteChatClick = {},
                onUnblockUserClick = {}
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Chat content")
                }
            }
        }
    }
}