package com.upsaclay.mission.presentation.components.form

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
import androidx.compose.runtime.getValue
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
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionTaskFixture
import java.time.LocalDate

@Composable
fun MissionForm(
    modifier: Modifier = Modifier,
    value: MissionFormValue,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onDurationChange: (String) -> Unit,
    onMaxParticipantsChange: (String) -> Unit,
    onShowManagerListClick: () -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (MissionTask) -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.mediumSpacing()
    ) {
        MissionFormImageSection(
            imageModel = value.imageReference,
            onImageClick = onImageClick,
            onRemoveImageClick = onRemoveImageClick
        )

        MissionFormTitleDescriptionSection(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            title = value.title,
            description = value.description,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionFormInformationSection(
            modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
            allSchoolLevels = value.allSchoolLevels,
            schoolLevels = value.schoolLevels,
            startDate = value.startDate,
            endDate = value.endDate,
            duration = value.duration,
            maxParticipants = value.maxParticipants,
            maxParticipantsError = value.maxParticipantsError,
            onSchoolLevelChange = onSchoolLevelChange,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick,
            onDurationChange = onDurationChange,
            onMaxParticipantsChange = onMaxParticipantsChange
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionFormManagerSection(
            managers = value.managers,
            onShowManagerListClick = onShowManagerListClick,
            onRemoveManagerClick = onRemoveManagerClick
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        MissionFormTaskSection(
            missionTasks = value.missionTasks,
            onAddTaskClick = onAddTaskClick,
            onTaskClick = onEditTaskClick,
            onRemoveTaskClick = onRemoveTaskClick
        )

        Spacer(Modifier.height(dimensionResource(com.upsaclay.common.R.dimen.default_padding)))
    }
}

data class MissionFormValue(
    val imageReference: String?,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val allSchoolLevels: List<SchoolLevel>,
    val schoolLevels: List<SchoolLevel>,
    val duration: String,
    val maxParticipants: String,
    val managers: List<User>,
    val missionTasks: List<MissionTask>,
    val maxParticipantsError: String? = null
)

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun MissionFormPreview() {
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
                    allSchoolLevels = SchoolLevel.getSchoolLevels(),
                    schoolLevels = selectedSchoolLevels,
                    startDate = LocalDate.now(),
                    endDate = LocalDate.now(),
                    duration = frequency,
                    maxParticipants = participantNumber,
                    managers = managers,
                    imageReference = null,
                    missionTasks = missionTasks
                ),
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onSchoolLevelChange = {},
                onStartDateClick = {},
                onEndDateClick = {},
                onDurationChange = { frequency = it },
                onMaxParticipantsChange = { participantNumber = it },
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
