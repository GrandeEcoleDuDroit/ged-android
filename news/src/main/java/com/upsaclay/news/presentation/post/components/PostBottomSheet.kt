package com.upsaclay.news.presentation.post.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.TextItem
import com.upsaclay.news.domain.post.Post.PostState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostBottomSheet(
    postState: PostState,
    isEditable: Boolean,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isEditable) {
        ModalBottomSheet(onDismissRequest = onDismiss) {
            Column(modifier = Modifier.navigationBarsPadding()) {
                when (postState) {
                    is PostState.Published -> {
                        EditablePostBottomSheetContent(
                            onDeleteClick = onDeleteClick
                        )
                    }

                    is PostState.Error -> {
                        ErrorPostBottomSheetContent(
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
    onDeleteClick: () -> Unit
) {
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
    onDeleteClick: () -> Unit
) {
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