package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.news.R
import com.upsaclay.news.domain.post.Post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheet(
    postState: PostState,
    isEditable: Boolean,
    onEditClick: () -> Unit,
    onRecreateClick: () -> Unit = {},
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isEditable) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.navigationBarsPadding()) {
                when (postState) {
                    is PostState.Published -> {
                        EditablePostBottomSheetContent(
                            onEditClick = onEditClick,
                            onDeleteClick = onDeleteClick
                        )
                    }

                    is PostState.Error -> {
                        ErrorPostBottomSheetContent(
                            onRecreateClick = onRecreateClick,
                            onDeleteClick = onDeleteClick
                        )
                    }

                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun EditablePostBottomSheetContent(
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
private fun ErrorPostBottomSheetContent(
    onRecreateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TextItem(
        modifier = Modifier.fillMaxWidth(),
        text = { Text(text = stringResource(id = com.upsaclay.common.R.string.retry)) },
        icon = {
            Icon(
                imageVector = Icons.Default.Refresh,
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