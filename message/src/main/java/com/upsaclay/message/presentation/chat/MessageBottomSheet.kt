package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.message.R
import com.upsaclay.message.domain.entity.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentMessageBottomSheet(
    messageState: Message.MessageState,
    onCopyClick: () -> Unit,
    onResendMessageClick: () -> Unit,
    onDeleteMessageClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            if (messageState != Message.MessageState.ERROR) {
                TextItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = stringResource(id = com.upsaclay.common.R.string.copy)) },
                    icon = {
                        Icon(
                            painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_copy),
                            contentDescription = null
                        )
                    },
                    onClick = onCopyClick
                )
            } else {
                TextItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = stringResource(id = R.string.resend)) },
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = null
                        )
                    },
                    onClick = onResendMessageClick
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
                    onClick = onDeleteMessageClick
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedMessageBottomSheet(
    onCopyClick: () -> Unit,
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = { Text(text = stringResource(id = com.upsaclay.common.R.string.copy)) },
                icon = {
                    Icon(
                        painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_copy),
                        contentDescription = null
                    )
                },
                onClick = onCopyClick
            )

            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = {
                    Text(
                        text = stringResource(id = com.upsaclay.common.R.string.report),
                        color = MaterialTheme.colorScheme.error
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_report),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                },
                onClick = onReportClick
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(heightDp = 400)
@Composable
private fun SentMessageBottomSheetPreview() {
    GedoiseTheme {
        SentMessageBottomSheet(
            messageState = Message.MessageState.SENT,
            onCopyClick = {},
            onResendMessageClick = {},
            onDeleteMessageClick = {},
            onDismiss = {}
        )
    }
}

@Preview(heightDp = 400)
@Composable
private fun ReceivedMessageBottomSheetPreview() {
    GedoiseTheme {
        ReceivedMessageBottomSheet(
            onCopyClick = {},
            onReportClick = {},
            onDismiss = {}
        )
    }
}