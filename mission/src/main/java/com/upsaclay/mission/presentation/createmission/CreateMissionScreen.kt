package com.upsaclay.mission.presentation.createmission

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.components.DatePickerModal
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionBottomSheetType
import com.upsaclay.mission.presentation.components.bottomsheet.AddTaskModalBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheet.EditTaskModalBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheet.SelectManagerModalBottomSheet
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun CreateMissionDestination(
    onBackClick: () -> Unit,
    viewModel: CreateMissionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateMissionScreen(
        title = uiState.title,
        description = uiState.description,
        allSchoolLevels = uiState.allSchoolLevels,
        schoolLevels = uiState.schoolLevels,
        startDate = uiState.startDate,
        endDate = uiState.endDate,
        duration = uiState.duration,
        maxParticipants = uiState.maxParticipants,
        missionTasks = uiState.tasks,
        imageUri = uiState.imageUri,
        users = uiState.users,
        userQuery = uiState.userQuery,
        managers = uiState.managers,
        createEnabled = uiState.createEnabled,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onSchoolLevelChange = viewModel::onSchoolLevelChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onDurationChange = viewModel::onDurationChange,
        onMaxParticipantsChange = viewModel::onMaxParticipantsChange,
        onSaveManagersClick = viewModel::onSaveManagers,
        onRemoveManagerClick = viewModel::onRemoveManager,
        onUserQueryChange = viewModel::onUserQueryChange,
        onResetUserQuery = viewModel::onResetUserQuery,
        onImageUriChange = viewModel::onImageUriChange,
        onRemoveImageClick = viewModel::onRemoveImageUri,
        onAddTaskClick = viewModel::onAddTask,
        onEditTaskClick = viewModel::onEditTask,
        onRemoveTaskClick = viewModel::onRemoveTask,
        onCreateMissionClick = {
            viewModel.createMission()
            onBackClick()
        },
        onBackClick = onBackClick
    )
}

@Composable
private fun CreateMissionScreen(
    title: String,
    description: String,
    allSchoolLevels: List<SchoolLevel>,
    schoolLevels: List<SchoolLevel>,
    startDate: LocalDate,
    endDate: LocalDate,
    duration: String,
    maxParticipants: String,
    missionTasks: List<MissionTask>,
    imageUri: Uri?,
    users: List<User>,
    userQuery: String,
    managers: List<User>,
    createEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onDurationChange: (String) -> Unit,
    onMaxParticipantsChange: (String) -> Unit,
    onSaveManagersClick: (List<User>) -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onUserQueryChange: (String) -> Unit,
    onResetUserQuery: () -> Unit,
    onImageUriChange: (Uri) -> Unit,
    onRemoveImageClick: () -> Unit,
    onAddTaskClick: (MissionTask) -> Unit,
    onEditTaskClick: (MissionTask) -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit,
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showStartDateModal by remember { mutableStateOf(false) }
    var showEndDateModal by remember { mutableStateOf(false) }
    var bottomSheetType by remember { mutableStateOf<MissionBottomSheetType?>(null) }
    val focusManager = LocalFocusManager.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { it?.let(onImageUriChange) }
    )

    Scaffold(
        topBar = {
            EditTopBar(
                title = stringResource(R.string.new_mission),
                onCancelClick = {
                    focusManager.clearFocus()
                    onBackClick()
                },
                onActionClick = onCreateMissionClick,
                actionLabel = stringResource(com.upsaclay.common.R.string.publish),
                buttonEnable = createEnabled
            )
        }
    ) { innerPadding ->
        MissionForm(
            modifier = Modifier
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            value = MissionFormValue(
                title = title,
                description = description,
                allSchoolLevels = allSchoolLevels,
                schoolLevels = schoolLevels,
                startDate = startDate,
                endDate = endDate,
                duration = duration,
                maxParticipants = maxParticipants,
                managers = managers,
                tasks = missionTasks,
                state = MissionState.Draft(imageUri?.toString())
            ),
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onSchoolLevelChange = onSchoolLevelChange,
            onStartDateClick = { showStartDateModal = true },
            onEndDateClick = { showEndDateModal = true },
            onDurationChange = onDurationChange,
            onMaxParticipantsChange = onMaxParticipantsChange,
            onShowManagerListClick = { bottomSheetType = MissionBottomSheetType.SelectManager },
            onRemoveManagerClick = onRemoveManagerClick,
            onAddTaskClick = { bottomSheetType = MissionBottomSheetType.AddTask },
            onEditTaskClick = { bottomSheetType = MissionBottomSheetType.EditTask(it) },
            onRemoveTaskClick = onRemoveTaskClick,
            onImageClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onRemoveImageClick = onRemoveImageClick
        )
    }

    if (showStartDateModal) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                if (selectedDate != null) {
                    onStartDateChange(selectedDate)
                }
                showStartDateModal = false
            },
            onDismiss = { showStartDateModal = false }
        )
    }

    if (showEndDateModal) {
        DatePickerModal(
            onDateSelected = { selectedDate ->
                if (selectedDate != null) {
                    onEndDateChange(selectedDate)
                }
                showEndDateModal = false
            },
            startDateLimit = startDate,
            onDismiss = { showEndDateModal = false }
        )
    }

    when (bottomSheetType) {
        is MissionBottomSheetType.AddTask -> {
            AddTaskModalBottomSheet(
                onDismissRequest = { bottomSheetType = null },
                onAddClick = { task ->
                    onAddTaskClick(task)
                    bottomSheetType = null
                }
            )
        }

        is MissionBottomSheetType.EditTask -> {
            (bottomSheetType as? MissionBottomSheetType.EditTask)?.missionTask?.let {
                EditTaskModalBottomSheet(
                    initialMissionTask = it,
                    onDismissRequest = { bottomSheetType = null },
                    onEditClick = { task ->
                        onEditTaskClick(task)
                        bottomSheetType = null
                    }
                )
            }
        }

        is MissionBottomSheetType.SelectManager -> {
            SelectManagerModalBottomSheet(
                users = users,
                selectedManagers = managers,
                userQuery = userQuery,
                onUserQueryChange = onUserQueryChange,
                onResetQuery = onResetUserQuery,
                onSaveClick = {
                    onSaveManagersClick(it)
                    bottomSheetType = null
                },
                onDismissRequest = {
                    onResetUserQuery()
                    bottomSheetType = null
                }
            )
        }

        null -> Unit
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionScreenPreview() {
    val mission = missionFixture

    GedoiseTheme {
        CreateMissionScreen(
            title = mission.title,
            description = mission.description,
            allSchoolLevels = SchoolLevel.entries,
            schoolLevels = mission.schoolLevels,
            startDate = mission.startDate,
            endDate = mission.endDate,
            duration = mission.duration.toString(),
            maxParticipants = mission.maxParticipants.toString(),
            managers = listOf(userFixture),
            missionTasks = mission.tasks,
            imageUri = null,
            users = usersFixture,
            userQuery = "",
            createEnabled = false,
            onTitleChange = {},
            onDescriptionChange = {},
            onSchoolLevelChange = {},
            onRemoveManagerClick = {},
            onStartDateChange = {},
            onEndDateChange = {},
            onDurationChange = {},
            onMaxParticipantsChange = {},
            onSaveManagersClick = {},
            onUserQueryChange = {},
            onResetUserQuery = {},
            onImageUriChange = {},
            onRemoveImageClick = {},
            onAddTaskClick = {},
            onEditTaskClick = {},
            onRemoveTaskClick = {},
            onCreateMissionClick = {},
            onBackClick = {},
        )
    }
}