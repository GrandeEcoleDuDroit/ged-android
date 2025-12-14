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
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionBottomSheetType
import com.upsaclay.mission.presentation.components.bottomsheets.AddMissionTaskBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheets.EditMissionTaskBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheets.SelectManagerBottomSheet
import com.upsaclay.mission.presentation.components.form.MissionForm
import com.upsaclay.mission.presentation.components.form.MissionFormValue
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
        startDate = uiState.startDate,
        endDate = uiState.endDate,
        allSchoolLevels = uiState.allSchoolLevels,
        schoolLevels = uiState.schoolLevels,
        duration = uiState.duration,
        maxParticipants = uiState.maxParticipants,
        imageUri = uiState.imageUri,
        users = uiState.users,
        userQuery = uiState.userQuery,
        managers = uiState.managers,
        missionTasks = uiState.missionTasks,
        createEnabled = uiState.createEnabled,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onSchoolLevelChange = viewModel::onSchoolLevelChange,
        onMaxParticipantsChange = viewModel::onMaxParticipantsChange,
        onDurationChange = viewModel::onDurationChange,
        onSaveManagersClick = viewModel::onSaveManagers,
        onRemoveManagerClick = viewModel::onRemoveManager,
        onUserQueryChange = viewModel::onUserQueryChange,
        onResetUserQuery = viewModel::onResetUserQuery,
        onImageUriChange = viewModel::onImageUriChange,
        onRemoveImageClick = viewModel::onRemoveImageUri,
        onAddTaskClick = viewModel::onAddMissionTask,
        onEditTaskClick = viewModel::onEditMissionTask,
        onRemoveTaskClick = viewModel::onRemoveMissionTask,
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
    imageUri: Uri?,
    users: List<User>,
    userQuery: String,
    managers: List<User>,
    missionTasks: List<MissionTask>,
    createEnabled: Boolean,
    onImageUriChange: (Uri) -> Unit,
    onRemoveImageClick: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onMaxParticipantsChange: (String) -> Unit,
    onDurationChange: (String) -> Unit,
    onSaveManagersClick: (List<User>) -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onUserQueryChange: (String) -> Unit,
    onResetUserQuery: () -> Unit,
    onAddTaskClick: (String) -> Unit,
    onEditTaskClick: (MissionTask) -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit,
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showStartDateModal by remember { mutableStateOf(false) }
    var showEndDateModal by remember { mutableStateOf(false) }
    var missionBottomSheetType by remember { mutableStateOf<MissionBottomSheetType?>(null) }
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
                imageReference = imageUri?.toString(),
                title = title,
                description = description,
                startDate = startDate,
                endDate = endDate,
                allSchoolLevels = allSchoolLevels,
                schoolLevels = schoolLevels,
                duration = duration,
                maxParticipants = maxParticipants,
                managers = managers,
                missionTasks = missionTasks
            ),
            onImageClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onRemoveImageClick = onRemoveImageClick,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onSchoolLevelChange = onSchoolLevelChange,
            onStartDateClick = { showStartDateModal = true },
            onEndDateClick = { showEndDateModal = true },
            onDurationChange = onDurationChange,
            onMaxParticipantsChange = onMaxParticipantsChange,
            onShowManagerListClick = { missionBottomSheetType = MissionBottomSheetType.SelectManager },
            onRemoveManagerClick = onRemoveManagerClick,
            onAddTaskClick = { missionBottomSheetType = MissionBottomSheetType.AddTask },
            onEditTaskClick = { missionBottomSheetType = MissionBottomSheetType.EditTask(it) },
            onRemoveTaskClick = onRemoveTaskClick
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

    when (val type = missionBottomSheetType) {
        is MissionBottomSheetType.AddTask -> {
            AddMissionTaskBottomSheet(
                onDismissRequest = { missionBottomSheetType = null },
                onAddClick = {
                    onAddTaskClick(it)
                    missionBottomSheetType = null
                }
            )
        }

        is MissionBottomSheetType.EditTask -> {
            EditMissionTaskBottomSheet(
                missionTask = type.missionTask,
                onDismissRequest = { missionBottomSheetType = null },
                onEditClick = { task ->
                    onEditTaskClick(task)
                    missionBottomSheetType = null
                }
            )
        }

        is MissionBottomSheetType.SelectManager -> {
            SelectManagerBottomSheet(
                users = users,
                selectedManagers = managers,
                userQuery = userQuery,
                onUserQueryChange = onUserQueryChange,
                onResetQuery = onResetUserQuery,
                onSaveClick = {
                    onSaveManagersClick(it)
                    missionBottomSheetType = null
                },
                onDismissRequest = {
                    onResetUserQuery()
                    missionBottomSheetType = null
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

@PhonePreviews
@Composable
private fun CreateMissionScreenPreview() {
    val mission = missionFixture

    GedoiseTheme {
        CreateMissionScreen(
            title = "",
            description = "",
            startDate = mission.startDate,
            endDate = mission.endDate,
            allSchoolLevels = SchoolLevel.entries,
            schoolLevels = emptyList(),
            duration = "",
            maxParticipants = "",
            managers = listOf(userFixture),
            missionTasks = emptyList(),
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