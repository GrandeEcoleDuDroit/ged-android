package com.upsaclay.mission.presentation.components.bottomsheets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
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
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_TASK_LENGTH
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun AddMissionTaskBottomSheet(
    onDismissRequest: () -> Unit,
    onAddClick: (String) -> Unit
) {
    var createEnabled by remember { mutableStateOf(false) }

    MissionTaskBottomSheet(
        onValueChange = { createEnabled = it.isNotBlank() },
        buttonEnabled = createEnabled,
        buttonLabel = stringResource(R.string.add),
        onButtonClick = onAddClick,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun EditMissionTaskBottomSheet(
    missionTask: MissionTask,
    onDismissRequest: () -> Unit,
    onEditClick: (MissionTask) -> Unit
) {
    var editEnabled by remember { mutableStateOf(false) }

    MissionTaskBottomSheet(
        initialValue = missionTask.value,
        onValueChange = {
            editEnabled = it.isNotBlank() && it != missionTask.value
        },
        buttonEnabled = editEnabled,
        buttonLabel = stringResource(com.upsaclay.common.R.string.save),
        onButtonClick = { value ->
            onEditClick(missionTask.copy(value = value))
        },
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MissionTaskBottomSheet(
    initialValue: String = "",
    onValueChange: (String) -> Unit,
    buttonEnabled: Boolean,
    buttonLabel: String,
    onButtonClick: (String) -> Unit,
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
        Column(modifier = Modifier.navigationBarsPadding()) {
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
                onClick = { onButtonClick(textFieldValue.text) },
                enabled = buttonEnabled,
            ) {
                Text(
                    text = buttonLabel,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
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
private fun MissionTaskBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            MissionTaskBottomSheet(
                onValueChange = {},
                buttonEnabled = true,
                buttonLabel = stringResource(R.string.add),
                onButtonClick = {},
                onDismissRequest = {}
            )
        }
    }
}
