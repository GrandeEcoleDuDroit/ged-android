package com.upsaclay.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.padding
import com.upsaclay.common.utils.PhonePreviews

@Composable
fun DefaultDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    text: String,
    confirmText: String = stringResource(id = R.string.accept),
    cancelText: String = stringResource(id = R.string.cancel),
    critical: Boolean = false,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        title = title?.let { { Text(text = title) } },
        text = { Text(text = text) },
        onDismissRequest = onCancel,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    color = if (critical) MaterialTheme.colorScheme.error else Color.Unspecified
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text(text = cancelText)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingDialog(message: String = stringResource(id = R.string.loading)) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    BasicAlertDialog(
        onDismissRequest = { },
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.extraSmall
            )
            .widthIn(max = screenWidth * 0.4f)
            .padding(vertical = MaterialTheme.padding.mediumLarge)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressBar(scale = 0.5f)

            Spacer(modifier = Modifier.width(MaterialTheme.padding.medium))

            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun SimpleDialogPreview() {
    GedoiseTheme {
        DefaultDialog(
            title = "Simple dialog",
            text = "There is the text area",
            confirmText = "Confirm",
            cancelText = "Cancel",
            onConfirm = {},
            onCancel = {},
        )
    }
}

@PhonePreviews
@Composable
private fun CriticalDialogPreview() {
    GedoiseTheme {
        DefaultDialog(
            title = "Sensible action",
            text = "Do you want to do this sensible action ?",
            confirmText = "Delete",
            cancelText = "Cancel",
            critical = true,
            onConfirm = {},
            onCancel = {}
        )
    }
}

@PhonePreviews
@Composable
private fun LoadingDialogPreview() {
    GedoiseTheme {
        LoadingDialog(
            message = "Waiting..."
        )
    }
}