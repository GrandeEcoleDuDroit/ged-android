package com.upsaclay.mission.presentation.editmission

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.DatePickerModal
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.presentation.MissionBottomSheetType
import com.upsaclay.mission.presentation.components.bottomsheet.AddTaskModalBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheet.EditTaskModalBottomSheet
import com.upsaclay.mission.presentation.components.bottomsheet.SelectManagerModalBottomSheet
import com.upsaclay.mission.presentation.createmission.MissionForm
import com.upsaclay.mission.presentation.createmission.MissionFormValue
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalDate

@Composable
fun EditMissionDestination(
    mission: Mission,
    onBackClick: () -> Unit,
    viewModel: EditMissionViewModel = koinViewModel(
        parameters = { parametersOf(mission) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> scope.launch {
                    snackbarHostState.showSnackbar(context.getString(event.messageId))
                }

                is SingleUiEvent.Success -> onBackClick()
            }
        }
    }

    EditMissionScreen(
        title = uiState.title,
        description = uiState.description,
        startDate = uiState.startDate,
        endDate = uiState.endDate,
        allSchoolLevels = uiState.allSchoolLevels,
        schoolLevels = uiState.schoolLevels,
        duration = uiState.duration,
        maxParticipants = uiState.maxParticipants,
        missionTasks = uiState.tasks,
        missionState = uiState.state,
        users = uiState.users,
        userQuery = uiState.userQuery,
        imageUri = uiState.imageUri,
        managers = uiState.managers,
        loading = uiState.loading,
        editEnabled = uiState.updateEnabled,
        snackbarHostState = snackbarHostState,
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
        onRemoveImageClick = viewModel::onRemoveImage,
        onAddTaskClick = viewModel::onAddTask,
        onEditTaskClick = viewModel::onEditTask,
        onRemoveTaskClick = viewModel::onRemoveTask,
        onSaveMissionClick = viewModel::updateMission,
        onBackClick = onBackClick
    )
}

@Composable
private fun EditMissionScreen(
    title: String,
    description: String,
    startDate: LocalDate,
    endDate: LocalDate,
    allSchoolLevels: List<SchoolLevel>,
    schoolLevels: List<SchoolLevel>,
    duration: String,
    maxParticipants: String,
    missionTasks: List<MissionTask>,
    missionState: MissionState,
    users: List<User>,
    userQuery: String,
    imageUri: Uri?,
    managers: List<User>,
    loading: Boolean,
    editEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
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
    onAddTaskClick: (String) -> Unit,
    onEditTaskClick: (MissionTask) -> Unit,
    onRemoveTaskClick: (MissionTask) -> Unit,
    onSaveMissionClick: () -> Unit,
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

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            EditTopBar(
                title = stringResource(R.string.edit_mission),
                onCancelClick = {
                    focusManager.clearFocus()
                    onBackClick()
                },
                onActionClick = onSaveMissionClick,
                actionLabel = stringResource(com.upsaclay.common.R.string.save),
                buttonEnable = editEnabled
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(snackbarData = it)
            }
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
                startDate = startDate,
                endDate = endDate,
                allSchoolLevels = allSchoolLevels,
                schoolLevels = schoolLevels,
                duration = duration,
                maxParticipants = maxParticipants,
                managers = managers,
                tasks = missionTasks,
                imageReference = imageUri?.toString() ?: missionState.imageReference
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
                onAddClick = {
                    onAddTaskClick(it)
                    bottomSheetType = null
                }
            )
        }

        is MissionBottomSheetType.EditTask -> {
            (bottomSheetType as? MissionBottomSheetType.EditTask)?.missionTask?.let {
                EditTaskModalBottomSheet(
                    initialTask = it,
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
        EditMissionScreen(
            title = mission.title,
            description = mission.description,
            startDate = mission.startDate,
            endDate = mission.endDate,
            allSchoolLevels = SchoolLevel.entries,
            schoolLevels = mission.schoolLevels,
            duration = mission.duration.toString(),
            maxParticipants = mission.maxParticipants.toString(),
            managers = listOf(userFixture),
            missionTasks = mission.tasks,
            missionState = MissionState.Published(),
            users = usersFixture,
            userQuery = "",
            imageUri = null,
            loading = false,
            editEnabled = false,
            snackbarHostState = SnackbarHostState(),
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
            onSaveMissionClick = {},
            onBackClick = {},
        )
    }
}