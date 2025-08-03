package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.extension.largeSpacing
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.taskFixture
import java.time.LocalDate

@Composable
fun CreateMissionForm(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDate,
    endDate: LocalDate,
    frequency: String,
    selectedManagers: List<User>,
    tasks: List<Task>,
    imageUri: Uri?,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectedSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    onFrequencyChange: (String) -> Unit,
    onShowManagerListClick: () -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onAddTaskClick: () -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit,
    onImageClick: () -> Unit,
    onRemoveImageClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.largeSpacing()
    ) {
        CreateMissionImageSection(
            imageUri = imageUri,
            onImageClick = onImageClick,
            onRemoveImageClick = onRemoveImageClick
        )

        CreateMissionTitleDescriptionSection(
            title = title,
            description = description,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            scrollState = scrollState
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        CreateMissionInformationSection(
            schoolLevels = schoolLevels,
            selectedSchoolLevels = selectedSchoolLevels,
            startDate = startDate,
            endDate = endDate,
            frequency = frequency,
            onSelectedSchoolLevelsChange = onSelectedSchoolLevelChange,
            onStartDateClick = onStartDateClick,
            onEndDateClick = onEndDateClick,
            onFrequencyChange = onFrequencyChange,
            scrollState = scrollState
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        CreateMissionManagerSection(
            managers = selectedManagers,
            onShowManagerListClick = onShowManagerListClick,
            onRemoveManagerClick = onRemoveManagerClick
        )

        HorizontalDivider(modifier = Modifier.padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding)))

        CreateMissionTaskSection(
            tasks = tasks,
            scrollState = scrollState,
            onAddTaskClick = onAddTaskClick,
            onEditTaskClick = onEditTaskClick,
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
    var selectedSchoolLevels by remember { mutableStateOf(emptyList<SchoolLevel>()) }
    var frequency by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<Task>(taskFixture)) }
    var managers by remember { mutableStateOf(listOf(userFixture, userFixture2)) }

    GedoiseTheme {
        Surface {
            CreateMissionForm(
                title = title,
                description = description,
                schoolLevels = SchoolLevel.entries,
                selectedSchoolLevels = selectedSchoolLevels,
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                frequency = frequency,
                selectedManagers = managers,
                imageUri = null,
                tasks = tasks,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onSelectedSchoolLevelChange = {
                    selectedSchoolLevels = if (selectedSchoolLevels.contains(it)) {
                        selectedSchoolLevels - it
                    } else {
                        selectedSchoolLevels + it
                    }
                },
                onStartDateClick = {},
                onEndDateClick = {},
                onFrequencyChange = { frequency = it },
                onShowManagerListClick = { managers = managers + userFixture },
                onRemoveManagerClick = { managers = managers - it },
                onAddTaskClick = { tasks += taskFixture },
                onEditTaskClick = {},
                onRemoveTaskClick = { tasks = tasks - it },
                onImageClick = {},
                onRemoveImageClick = {}
            )
        }
    }
}
