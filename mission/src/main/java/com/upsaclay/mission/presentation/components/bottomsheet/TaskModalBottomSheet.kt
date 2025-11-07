package com.upsaclay.mission.presentation.components.bottomsheet

import androidx.compose.foundation.layout.Column
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
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTaskFixture
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay

@Composable
fun AddTaskModalBottomSheet(
    onDismissRequest: () -> Unit,
    onAddClick: (MissionTask) -> Unit
) {
    var missionTask by remember { mutableStateOf(MissionTask(GenerateIdUseCase.longId, "")) }
    var createEnabled by remember { mutableStateOf(false) }

    TaskModalBottomSheet(
        missionTask = missionTask,
        onValueChange = {
            missionTask = missionTask.copy(value = it.take(200))
            createEnabled = missionTask.value.isNotBlank()
        },
        enabled = createEnabled,
        labelButton = stringResource(R.string.add),
        onClick = onAddClick,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun EditTaskModalBottomSheet(
    initialMissionTask: MissionTask,
    onDismissRequest: () -> Unit,
    onEditClick: (MissionTask) -> Unit
) {
    var task by remember { mutableStateOf(initialMissionTask) }
    var editEnabled by remember { mutableStateOf(false) }

    TaskModalBottomSheet(
        missionTask = task,
        onValueChange = {
            task = task.copy(value = it)
            editEnabled = task.value.isNotBlank() && task.value != initialMissionTask.value
        },
        enabled = editEnabled,
        labelButton = stringResource(com.upsaclay.common.R.string.save),
        onClick = onEditClick,
        onDismissRequest = onDismissRequest
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskModalBottomSheet(
    missionTask: MissionTask,
    onValueChange: (String) -> Unit,
    enabled: Boolean,
    labelButton: String,
    onClick: (MissionTask) -> Unit,
    onDismissRequest: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = missionTask.value,
                selection = TextRange(missionTask.value.length)
            )
        )
    }

    val state = rememberModalBottomSheetState(
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                onDismissRequest()
                true
            } else {
                false
            }
        }
    )

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = state
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .fillMaxWidth(),
                value = textFieldValue,
                onValueChange = {
                    if (it.text.length < 100) {
                        textFieldValue = it
                        onValueChange(it.text)
                    }
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
                onClick = { onClick(missionTask.copy(value = missionTask.value.trim())) },
                enabled = enabled,
            ) {
                Text(
                    text = labelButton,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))
    }

    LaunchedEffect(Unit) {
        awaitFrame()
        delay(200)
        focusRequester.requestFocus()
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun EditTaskBottomSheetPreview() {
    GedoiseTheme {
        Surface {
            EditTaskModalBottomSheet(
                initialMissionTask = missionTaskFixture,
                onDismissRequest = {},
                onEditClick = {}
            )
        }
    }
}
