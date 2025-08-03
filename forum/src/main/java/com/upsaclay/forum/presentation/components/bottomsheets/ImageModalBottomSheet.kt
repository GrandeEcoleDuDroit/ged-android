package com.upsaclay.forum.presentation.components.bottomsheets

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.upsaclay.common.presentation.components.ClickableItem
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.forum.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageModalBottomSheet(
    onNewProfilePictureClick: () -> Unit,
    showDeleteImage: Boolean = false,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val hideBottomSheet = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                onDismiss()
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        ClickableItem(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = stringResource(id = R.string.new_image)) },
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

        if (showDeleteImage) {
            ClickableItem(
                modifier = Modifier.fillMaxWidth(),
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
                    onDeleteClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.large_padding)))
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ImageModalBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            ImageModalBottomSheet(
                onNewProfilePictureClick = {},
                onDeleteClick = {},
                onDismiss = {}
            )
        }
    }
}