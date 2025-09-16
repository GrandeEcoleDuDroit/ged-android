package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.news.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementBottomSheet(
    isEditable: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.testTag(stringResource(id = R.string.announcement_bottom_sheet_tag)),
        onDismissRequest = onDismiss
    ) {
        if (isEditable) {
            EditableAnnouncementBottomSheetContent(
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        } else {
            NonEditableAnnouncementBottomSheetContent(
                onReportClick = onReportClick
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorAnnouncementBottomSheet(
    onResendClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.testTag(stringResource(id = R.string.announcement_bottom_sheet_tag)),
        onDismissRequest = onDismiss
    ) {
        ClickableItem(
            modifier = Modifier.fillMaxWidth(),
            text = {
                Text(text = stringResource(id = com.upsaclay.common.R.string.resend))
            },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            },
            onClick = onResendClick
        )

        ClickableItem(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(stringResource(id = R.string.announcement_bottom_sheet_delete_field_tag)),
            text = {
                Text(
                    text = stringResource(id = com.upsaclay.common.R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            onClick = onDeleteClick
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

@Composable
private fun EditableAnnouncementBottomSheetContent(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    ClickableItem(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.read_screen_sheet_edit_field_tag)),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.edit)) },
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        },
        onClick = onEditClick
    )

    ClickableItem(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.announcement_bottom_sheet_delete_field_tag)),
        text = {
            Text(
                text = stringResource(id = com.upsaclay.common.R.string.delete),
                color = MaterialTheme.colorScheme.error
            )
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onDeleteClick
    )
}

@Composable
private fun NonEditableAnnouncementBottomSheetContent(
    onReportClick: () -> Unit
) {
    ClickableItem(
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Preview(heightDp = 400)
@Composable
fun EditableAnnouncementBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementBottomSheet(
                isEditable = true,
                onEditClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun NonEditableAnnouncementBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementBottomSheet(
                isEditable = false,
                onEditClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun ErrorAnnouncementBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            ErrorAnnouncementBottomSheet(
                onResendClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}