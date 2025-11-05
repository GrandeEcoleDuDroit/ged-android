package com.upsaclay.mission.presentation.components.formsection

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.components.SimpleOutlinedTextField
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.presentation.components.OutlinedDatePicker
import com.upsaclay.mission.presentation.components.OutlinedSchoolLevelDropDownMenu
import com.upsaclay.mission.presentation.components.item.SectionTitle
import java.time.LocalDate

@Composable
fun MissionInformationFormSection(
    modifier: Modifier = Modifier,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDate,
    endDate: LocalDate,
    duration: String,
    participantNumber: String,
    onSelectedSchoolLevelsChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onDurationChange: (String) -> Unit,
    onParticipantNumberChange: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        SectionTitle(title = stringResource(R.string.information))

        OutlinedSchoolLevelDropDownMenu(
            modifier = Modifier.fillMaxWidth(),
            schoolLevels = schoolLevels,
            selectedSchoolLevels = selectedSchoolLevels,
            onSelectedSchoolLevelsChange = onSelectedSchoolLevelsChange
        )

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

        SimpleOutlinedTextField(
            modifier = Modifier.fillMaxWidth(0.6f),
            value = participantNumber,
            onValueChange = onParticipantNumberChange,
            label = stringResource(R.string.mission_max_participant_field),
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionInformationSectionPreview() {
    var selectedSchoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    var duration by remember { mutableStateOf("") }
    var participantsNumber by remember { mutableStateOf("") }

    GedoiseTheme {
        Surface {
            MissionInformationFormSection(
                schoolLevels = SchoolLevel.entries.toList(),
                selectedSchoolLevels = selectedSchoolLevels,
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(7),
                duration = duration,
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
                onDurationChange = { duration = it },
                onParticipantNumberChange = { participantsNumber = it }
            )
        }
    }
}