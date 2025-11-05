package com.upsaclay.mission.presentation.createmission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.mediumSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTaskFixture
import com.upsaclay.mission.presentation.components.formsection.MissionImageFormSection
import com.upsaclay.mission.presentation.components.formsection.MissionInformationFormSection
import com.upsaclay.mission.presentation.components.formsection.MissionManagerFormSection
import com.upsaclay.mission.presentation.components.formsection.MissionTaskFormSection
import com.upsaclay.mission.presentation.components.formsection.MissionTitleDescriptionFormSection
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.delay
import java.time.LocalDate

@Composable
fun MissionForm(
    modifier: Modifier = Modifier,
    value: MissionFormValue,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectedSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFrequencyChange: (String) -> Unit,
    onParticipantNumberChange: (String) -> Unit,
    onShowManagerListClick: () -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (MissionTask) -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    val scrollState = rememberScrollState()
    var currentSize by remember { mutableIntStateOf(value.missionTasks.size) }

    LaunchedEffect(value.missionTasks) {
        if (value.missionTasks.size > currentSize) {
            awaitFrame()
            delay(200)
            scrollState.animateScrollTo(scrollState.maxValue)
            currentSize = value.missionTasks.size
        }
    }

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        MissionImageFormSection(
            imageUri = value.imageUri,
            onImageClick = onImageClick,
            onRemoveImageClick = onRemoveImageClick
        )

        MissionTitleDescriptionFormSection(
            title = value.title,
            description = value.description,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionInformationFormSection(
            schoolLevels = value.schoolLevels,
            selectedSchoolLevels = value.selectedSchoolLevels,
            startDate = value.startDate,
            endDate = value.endDate,
            duration = value.frequency,
            participantNumber = value.participantNumber,
            onSelectedSchoolLevelsChange = onSelectedSchoolLevelChange,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick,
            onDurationChange = onFrequencyChange,
            onParticipantNumberChange = onParticipantNumberChange
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionManagerFormSection(
            managers = value.selectedManagers,
            onShowManagerListClick = onShowManagerListClick,
            onRemoveManagerClick = onRemoveManagerClick
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionTaskFormSection(
            missionTasks = value.missionTasks,
            onAddTaskClick = onAddTaskClick,
            onTaskClick = onEditTaskClick,
            onRemoveTaskClick = onRemoveTaskClick
        )

        Spacer(modifier = Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.small_padding)))
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionFormPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val selectedSchoolLevels = emptyList<SchoolLevel>()
    var frequency by remember { mutableStateOf("") }
    var participantNumber by remember { mutableStateOf("") }
    var missionTasks by remember { mutableStateOf(listOf(missionTaskFixture)) }
    var managers by remember { mutableStateOf(listOf(userFixture, userFixture2)) }

    GedoiseTheme {
        Surface {
            MissionForm(
                value = MissionFormValue(
                    title = title,
                    description = description,
                    schoolLevels = SchoolLevel.getSchoolLevels(),
                    selectedSchoolLevels = selectedSchoolLevels,
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    frequency = frequency,
                    participantNumber = participantNumber,
                    selectedManagers = managers,
                    imageUri = null,
                    missionTasks = missionTasks
                ),
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onSelectedSchoolLevelChange = {},
                onStartDateClick = {},
                onEndDateClick = {},
                onFrequencyChange = { frequency = it },
                onParticipantNumberChange = { participantNumber = it },
                onShowManagerListClick = { managers = managers + userFixture },
                onRemoveManagerClick = { managers = managers - it },
                onAddTaskClick = { missionTasks += missionTaskFixture },
                onEditTaskClick = {},
                onRemoveTaskClick = { missionTasks = missionTasks - it },
                onImageClick = {},
                onRemoveImageClick = {}
            )
        }
    }
}
