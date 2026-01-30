package com.upsaclay.common.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.R
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    items: List<String>,
    onReportClick: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reportSheetContent by remember { mutableStateOf(ReportSheetContent.DEFAULT) }

    ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
        Column(modifier = Modifier.navigationBarsPadding()) {
            when (reportSheetContent) {
                ReportSheetContent.DEFAULT -> {
                    DefaultReportSheetContent(
                        items = items,
                        onReportClick = onReportClick,
                        onOtherReasonClick = { reportSheetContent = ReportSheetContent.OTHER }
                    )
                }

                ReportSheetContent.OTHER -> EditableBottomSheetContent(onSubmitClick = onReportClick)
            }
        }
    }
}

@Composable
private fun DefaultReportSheetContent(
    items: List<String>,
    onReportClick: (String) -> Unit,
    onOtherReasonClick: () -> Unit
) {
    Column {
        Text(
            text = stringResource(com.upsaclay.common.R.string.report),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))

        items.forEach { item ->
            TextItem(
                modifier = Modifier.fillMaxWidth(),
                text = { Text(text = item) },
                onClick = { onReportClick(item) }
            )
        }

        TextItem(
            modifier = Modifier.fillMaxWidth(),
            text = { Text(text = stringResource(R.string.other_report_reason)) },
            onClick = onOtherReasonClick
        )
    }
}

@Composable
private fun EditableBottomSheetContent(onSubmitClick: (String) -> Unit) {
    var value by remember { mutableStateOf("") }
    var submitEnabled by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val maxTextLength = 300

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(com.upsaclay.common.R.string.other_report_reason),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Column(modifier = Modifier.weight(1f)) {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .weight(1f)
                    .fillMaxWidth(),
                value = value,
                onValueChange = {
                    value = it.take(maxTextLength)
                    submitEnabled = it.isNotBlank()
                },
                keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
                placeholder = { Text(text = stringResource(R.string.other_report_reason_placeholder)) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                )
            )

            TextButton(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                onClick = { onSubmitClick(value) },
                enabled = submitEnabled,
            ) {
                Text(
                    text = stringResource(R.string.submit),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

private enum class ReportSheetContent {
    DEFAULT,
    OTHER
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