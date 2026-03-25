package com.upsaclay.news.presentation.announcement.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement.AnnouncementState
import com.upsaclay.news.domain.announcement.announcementFixture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementBottomSheet(
    announcementState: AnnouncementState,
    isEditable: Boolean,
    onEditClick: () -> Unit,
    onRecreateClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.testTag(stringResource(id = R.string.announcement_bottom_sheet_tag)),
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            when (announcementState) {
                AnnouncementState.PUBLISHED -> {
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
                }

                AnnouncementState.ERROR -> {
                    ErrorAnnouncementBottomSheetContent(
                        onResendClick = onRecreateClick,
                        onDeleteClick = onDeleteClick
                    )
                }

                AnnouncementState.PUBLISHING -> PublishingAnnouncementBottomSheetContent(
                    onDeleteClick = onDeleteClick
                )

                AnnouncementState.DRAFT -> Unit
            }
        }
    }
}

@Composable
private fun EditableAnnouncementBottomSheetContent(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.read_screen_sheet_edit_field_tag)),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.edit)) },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null
            )
        },
        onClick = onEditClick
    )

    TextItem(
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
                imageVector = Icons.Outlined.Delete,
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

@Composable
private fun PublishingAnnouncementBottomSheetContent(onDeleteClick: () -> Unit) {
    TextItem(
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
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
        },
        onClick = onDeleteClick
    )
}

@Composable
private fun ErrorAnnouncementBottomSheetContent(
    onResendClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = {
            Text(text = stringResource(id = com.upsaclay.common.R.string.retry))
        },
        icon = {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = null
            )
        },
        onClick = onResendClick
    )

    TextItem(
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
fun EditableAnnouncementBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            AnnouncementBottomSheet(
                announcementState = announcementFixture.state,
                isEditable = true,
                onEditClick = {},
                onRecreateClick = {},
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
                announcementState = announcementFixture.state,
                isEditable = false,
                onEditClick = {},
                onRecreateClick = {},
                onReportClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}

@Preview(heightDp = 400)
@Composable
fun ErrorAnnouncementBottomSheetContentPreview() {
    GedoiseTheme {
        Surface {
            ErrorAnnouncementBottomSheetContent(
                onResendClick = {},
                onDeleteClick = {}
            )
        }
    }
}