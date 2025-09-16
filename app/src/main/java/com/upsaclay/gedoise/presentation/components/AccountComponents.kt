package com.upsaclay.gedoise.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.gedoise.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountModelBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onNewProfilePictureClick: () -> Unit,
    showDeleteProfilePicture: Boolean = false,
    onDeleteProfilePictureClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val hideBottomSheet: () -> Unit = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        ClickableItem(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = stringResource(id = R.string.new_profile_picture)) },
            icon = {
                Icon(
                    painter = painterResource(id = com.upsaclay.common.R.drawable.ic_picture),
                    contentDescription = null
                )
            },
            onClick = {
                hideBottomSheet()
                onNewProfilePictureClick()
            }
        )

        if (showDeleteProfilePicture) {
            ClickableItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(stringResource(id = R.string.account_screen_delete_profile_picture_button_tag)),
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
                onClick = {
                    hideBottomSheet()
                    onDeleteProfilePictureClick()
                }
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AccountTopBar(
    isEdited: Boolean,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    onBackClick: () -> Unit
) {
    if (isEdited) {
        EditTopBar(
            title = stringResource(id = R.string.edit_profile),
            onCancelClick = onCancelClick,
            onActionClick = onSaveClick,
            actionLabel = stringResource(id = com.upsaclay.common.R.string.save)
        )
    } else {
        BackTopBar(
            title = stringResource(id = R.string.account_informations),
            onBackClick = onBackClick
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

