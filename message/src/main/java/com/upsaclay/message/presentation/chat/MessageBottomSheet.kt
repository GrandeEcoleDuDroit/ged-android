package com.upsaclay.message.presentation.chat

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.TextItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentMessageBottomSheet(
    onResendMessageClick: () -> Unit,
    onDeleteMessageClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        TextItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(text = stringResource(id = com.upsaclay.common.R.string.resend))
            },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
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

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.modal_bottom_sheet_bottom_space)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceivedMessageBottomSheet(
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
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

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.modal_bottom_sheet_bottom_space)))
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
    SentMessageBottomSheet(
        onResendMessageClick = {},
        onDeleteMessageClick = {},
        onDismiss = {}
    )
}

@Preview(heightDp = 400)
@Composable
private fun ReceivedMessageBottomSheetPreview() {
    ReceivedMessageBottomSheet(
        onReportClick = {},
        onDismiss = {}
    )
}