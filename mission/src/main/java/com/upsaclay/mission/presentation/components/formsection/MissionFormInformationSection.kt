package com.upsaclay.mission.presentation.components.formsection

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.bringIntoView
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.presentation.components.OutlinedDatePicker
import com.upsaclay.mission.presentation.components.OutlinedSchoolLevelDropDownMenu
import java.time.LocalDate

@Composable
fun MissionFormInformationSection(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDate,
    endDate: LocalDate,
    frequency: String,
    participantNumber: String,
    onSelectedSchoolLevelsChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFrequencyChange: (String) -> Unit,
    onParticipantNumberChange: (String) -> Unit,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        Text(
            text = stringResource(R.string.informations),
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedSchoolLevelDropDownMenu(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoView(scrollState),
            schoolLevels = schoolLevels,
            selectedSchoolLevels = selectedSchoolLevels,
            onSelectedSchoolLevelsChange = onSelectedSchoolLevelsChange
        )

        OutlinedDatePicker(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoView(scrollState),
            date = startDate,
            onClick = onStartDateClick,
            label = stringResource(R.string.start_date)
        )

        OutlinedDatePicker(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoView(scrollState),
            date = endDate,
            onClick = onEndDateClick,
            label = stringResource(R.string.end_date)
        )

        SelectionContainer {
            SimpleOutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .bringIntoView(scrollState),
                value = frequency,
                onValueChange = onFrequencyChange,
                label = stringResource(R.string.frequency),
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

        SimpleOutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = participantNumber,
            onValueChange = onParticipantNumberChange,
            label = stringResource(R.string.max_participant),
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
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionInformationSectionPreview() {
    var selectedSchoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    var frequency by remember { mutableStateOf("") }
    var participantsNumber by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            MissionFormInformationSection(
                schoolLevels = SchoolLevel.entries.toList(),
                selectedSchoolLevels = selectedSchoolLevels,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(7),
                frequency = frequency,
                participantNumber = participantsNumber,
                onSelectedSchoolLevelsChange = {
                    if (selectedSchoolLevels.contains(it)) {
                        selectedSchoolLevels = selectedSchoolLevels - it
                    } else {
                        selectedSchoolLevels = selectedSchoolLevels + it
                    }
                },
                onStartDateClick = {},
                onEndDateClick = {},
                onFrequencyChange = { frequency = it },
                onParticipantNumberChange = { participantsNumber = it },
                scrollState = ScrollState(0)
            )
        }
    }
}