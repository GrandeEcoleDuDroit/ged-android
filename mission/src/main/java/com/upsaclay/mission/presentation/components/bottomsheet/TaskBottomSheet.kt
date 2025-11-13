package com.upsaclay.mission.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTaskFixture
import com.upsaclay.mission.presentation.MissionConstants.MAX_TASK_LENGTH
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun AddTaskBottomSheet(
    onDismissRequest: () -> Unit,
    onAddClick: (String) -> Unit
) {
    var createEnabled by remember { mutableStateOf(false) }

    TaskBottomSheet(
        onValueChange = { createEnabled = it.isNotBlank() },
        enabled = createEnabled,
        labelButton = stringResource(R.string.add),
        onClick = onAddClick,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun EditTaskBottomSheet(
    initialTask: MissionTask,
    onDismissRequest: () -> Unit,
    onEditClick: (MissionTask) -> Unit
) {
    var editEnabled by remember { mutableStateOf(false) }

    TaskBottomSheet(
        initialValue = initialTask.value,
        onValueChange = {
            editEnabled = it.isNotBlank() && it != initialTask.value
        },
        enabled = editEnabled,
        labelButton = stringResource(com.upsaclay.common.R.string.save),
        onClick = { value ->
            onEditClick(initialTask.copy(value = value))
        },
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskBottomSheet(
    initialValue: String = "",
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    labelButton: String,
    onClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = initialValue,
                selection = TextRange(initialValue.length)
            )
        )
    }

    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                onDismissRequest()
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(Unit) {
        awaitFrame()
        delay(200)
        focusRequester.requestFocus()
    }

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = state
    ) {
        OutlinedTextField(
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f, fill = false)
                .fillMaxWidth(),
            value = textFieldValue,
            onValueChange = {
                val text = it.text.take(MAX_TASK_LENGTH)
                textFieldValue = it.copy(text = text)
                onValueChange(text)
            },
            keyboardOptions = KeyboardOptions(KeyboardCapitalization.Sentences),
            placeholder = { Text(text = stringResource(R.string.enter_task)) },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent
            )
        )

        TextButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            onClick = { onClick(textFieldValue.text) },
            enabled = enabled,
        ) {
            Text(
                text = labelButton,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
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

@PhonePreviews
@Composable
private fun EditTaskBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            EditTaskBottomSheet(
                initialTask = missionTaskFixture,
                onDismissRequest = {},
                onEditClick = {}
            )
        }
    }
}
