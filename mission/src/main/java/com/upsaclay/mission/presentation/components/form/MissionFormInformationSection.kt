package com.upsaclay.mission.presentation.components.form

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.components.MultiSelectionDropDownMenu
import com.upsaclay.common.presentation.components.SectionTitle
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.DateUtils
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import java.time.LocalDate

@Composable
fun MissionFormInformationSection(
    modifier: Modifier = Modifier,
    startDate: LocalDate,
    endDate: LocalDate,
    allSchoolLevels: List<SchoolLevel>,
    schoolLevels: List<SchoolLevel>,
    duration: String,
    maxParticipants: String,
    schoolLevelSupportingText: Int? = null,
    maxParticipantsError: String? = null,
    onSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onDurationChange: (String) -> Unit,
    onMaxParticipantsChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.information))

        OutlinedDatePicker(
            modifier = Modifier.fillMaxWidth(),
            date = startDate,
            onClick = onStartDateClick,
            label = stringResource(R.string.mission_start_date_field),
        )

        OutlinedDatePicker(
            modifier = Modifier.fillMaxWidth(),
            date = endDate,
            onClick = onEndDateClick,
            label = stringResource(R.string.mission_end_date_field),
        )

        OutlinedSchoolLevelDropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            allSchoolLevels = allSchoolLevels,
            schoolLevels = schoolLevels,
            onSchoolLevelChange = onSchoolLevelChange,
            schoolLevelSupportingText = schoolLevelSupportingText
        )

        SimpleOutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = maxParticipants,
            onValueChange = onMaxParticipantsChange,
            errorMessage = maxParticipantsError,
            label = stringResource(R.string.mission_max_participants_field),
            leadingIcon = {
                Icon(
                    painter = painterResource(com.upsaclay.common.R.drawable.ic_outline_group),
                    null
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        SelectionContainer {
            SimpleOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = duration,
                onValueChange = onDurationChange,
                label = stringResource(R.string.mission_duration_field),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_outline_schedule),
                        contentDescription = null,
                    )
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrectEnabled = true
                )
            )
        }
    }
}

@Composable
private fun OutlinedSchoolLevelDropDownMenu(
    modifier: Modifier = Modifier,
    allSchoolLevels: List<SchoolLevel>,
    schoolLevels: List<SchoolLevel>,
    schoolLevelSupportingText: Int? = null,
    onSchoolLevelChange: (SchoolLevel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val value = when {
        schoolLevels.isEmpty() -> stringResource(R.string.everyone)
        schoolLevels.size == allSchoolLevels.size -> stringResource(R.string.everyone)
        else -> schoolLevels.joinToString(" - ", transform = { it.value })
    }

    MultiSelectionDropDownMenu(
        modifier = modifier,
        items = allSchoolLevels.map { it.value },
        selectedItems = schoolLevels.map { it.value },
        value = value,
        label = stringResource(R.string.mission_school_level_field),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_outline_school),
                contentDescription = null,
            )
        },
        singleLine = true,
        onItemSelected = { SchoolLevel.fromValue(it).let(onSchoolLevelChange) },
        supportingText = schoolLevelSupportingText,
        expanded = expanded,
        onExpandedChange = { expanded = it },
        onDismissRequest = { expanded = false }
    )
}

@Composable
private fun OutlinedDatePicker(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onClick: () -> Unit,
    label: String,
) {
    SimpleOutlinedTextField(
        modifier = modifier
            .pointerInput(date) {
                awaitEachGesture {
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        onClick()
                    }
                }
            },
        value = DateUtils.formatDayMonthYear(date),
        onValueChange = {},
        readOnly = true,
        label = label,
        leadingIcon = {
            Icon(
                painter = painterResource(com.upsaclay.common.R.drawable.ic_fill_calendar),
                contentDescription = null,
            )
        }
    )
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionFormInformationSectionPreview() {
    var schoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    var duration by remember { mutableStateOf("") }
    var participantsNumber by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            MissionFormInformationSection(
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(7),
                allSchoolLevels = SchoolLevel.all,
                schoolLevels = schoolLevels,
                schoolLevelSupportingText = null,
                duration = duration,
                maxParticipants = participantsNumber,
                onSchoolLevelChange = {
                    schoolLevels = if (schoolLevels.contains(it)) {
                        schoolLevels - it
                    } else {
                        schoolLevels + it
                    }
                },
                onStartDateClick = {},
                onEndDateClick = {},
                onDurationChange = { duration = it },
                onMaxParticipantsChange = { participantsNumber = it }
            )
        }
    }
}

@PhonePreviews
@Composable
private fun SchoolLevelDropDownPreview() {
    var selectedSchoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    GedoiseTheme {
        Surface {
            OutlinedSchoolLevelDropDownMenu(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                allSchoolLevels = SchoolLevel.all,
                schoolLevels = emptyList() ,
                onSchoolLevelChange = {
                    selectedSchoolLevels = if (selectedSchoolLevels.contains(it)) {
                        selectedSchoolLevels - it
                    } else {
                        selectedSchoolLevels + it
                    }
                }
            )
        }
    }
}

@PhonePreviews
@Composable
private fun OutlinedDatePickerPreview() {
    GedoiseTheme {
        Surface {
            OutlinedDatePicker(
                modifier = Modifier.padding(dimensionResource(com.upsaclay.common.R.dimen.small_padding)),
                date = LocalDate.now(),
                onClick = {},
                label = stringResource(R.string.start_date)
            )
        }
    }
}