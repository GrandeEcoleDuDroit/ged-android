package com.upsaclay.message.presentation.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.message.domain.entity.Conversation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationBottomSheet(
    conversationState: Conversation.ConversationState,
    onRecreateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            if (conversationState == Conversation.ConversationState.ERROR) {
                ErrorConversationBottomSheetContent(
                    onRecreateClick = onRecreateClick,
                    onDeleteClick = onDeleteClick
                )
            } else {
                DefaultConversationBottomSheetContent(
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
private fun DefaultConversationBottomSheetContent(onDeleteClick: () -> Unit) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onDeleteClick
    )
}

@Composable
private fun ErrorConversationBottomSheetContent(
    onRecreateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(text = stringResource(id = com.upsaclay.common.R.string.retry))
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Refresh,
                contentDescription = null
            )
        },
        onClick = onRecreateClick
    )

    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onDeleteClick
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(heightDp = 400)
@Composable
private fun ConversationBottomSheetPreview() {
    GedoiseTheme {
        ConversationBottomSheet(
            conversationState = Conversation.ConversationState.CREATED,
            onRecreateClick = {},
            onDeleteClick = {},
            onDismiss = {},
        )
    }
}
