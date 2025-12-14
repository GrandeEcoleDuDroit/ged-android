package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T>ReportBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    items: List<T>,
    onReportClick: (T) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Text(
            text = stringResource(com.upsaclay.common.R.string.report),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))

        items.forEach {
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = { Text(text = it.toString()) },
                onClick = { onReportClick(it) }
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.modal_bottom_sheet_bottom_space)))
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@OptIn(ExperimentalMaterial3Api::class)
@PhonePreviews
@Composable
private fun PreviewReportBottomSheet() {
    GedoiseTheme {
        ReportBottomSheet(
            onDismiss = {},
            items = listOf("Spam", "Inappropriate", "Misleading"),
            onReportClick = {}
        )
    }
}