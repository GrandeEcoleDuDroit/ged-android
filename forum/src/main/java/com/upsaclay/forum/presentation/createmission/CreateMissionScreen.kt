package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.MissionBottomSheetType
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.tasksFixture
import com.upsaclay.forum.presentation.components.bottomsheets.AddTaskModalBottomSheet
import com.upsaclay.forum.presentation.components.bottomsheets.EditTaskModalBottomSheet
import com.upsaclay.forum.presentation.components.bottomsheets.ImageModalBottomSheet
import com.upsaclay.forum.presentation.components.bottomsheets.SelectManagerModalBottomSheet
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

@Composable
fun CreateMissionDestination(
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CreateMissionViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateMissionScreen(
        title = uiState.title,
        description = uiState.description,
        schoolLevels = uiState.schoolLevels,
        selectedSchoolLevels = uiState.selectedSchoolLevels,
        startDate = uiState.startDate,
        endDate = uiState.endDate,
        frequency = uiState.frequency,
        tasks = uiState.tasks.values.toList(),
        imageUri = uiState.imageUri,
        users = uiState.users,
        userQuery = uiState.userQuery,
        selectedManagers = uiState.selectedManagers,
        createEnabled = uiState.createEnabled,
        onTitleChange = viewModel::onTitleChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onSelectedSchoolLevelChange = viewModel::onSelectedSchoolLevelChange,
        onStartDateChange = viewModel::onStartDateChange,
        onEndDateChange = viewModel::onEndDateChange,
        onFrequencyChange = viewModel::onFrequencyChange,
        onSaveSelectedMangers = viewModel::onSaveSelectedManagers,
        onRemoveManagerClick = viewModel::onRemoveManager,
        onUserQueryChange = viewModel::onUserQueryChange,
        onResetUserQuery = viewModel::onResetUserQuery,
        onImageUriChange = viewModel::onMissionImageUriChange,
        onRemoveMissionImageClick = viewModel::onRemoveImageUri,
        onAddTaskClick = viewModel::onAddTask,
        onEditTaskClick = viewModel::onEditTask,
        onRemoveTaskClick = viewModel::onRemoveTask,
        onCreateMissionClick = onCreateMissionClick,
        onBackClick = onBackClick
    )
}

@Composable
fun CreateMissionScreen(
    title: String,
    description: String,
    schoolLevels: List<SchoolLevel>,
    selectedSchoolLevels: List<SchoolLevel>,
    startDate: LocalDate,
    endDate: LocalDate,
    frequency: String,
    tasks: List<Task>,
    imageUri: Uri?,
    users: List<User>,
    userQuery: String,
    selectedManagers: List<User>,
    createEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSelectedSchoolLevelChange: (SchoolLevel) -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onEndDateChange: (LocalDate) -> Unit,
    onFrequencyChange: (String) -> Unit,
    onSaveSelectedMangers: (List<User>) -> Unit,
    onRemoveManagerClick: (User) -> Unit,
    onUserQueryChange: (String) -> Unit,
    onResetUserQuery: () -> Unit,
    onImageUriChange: (Uri?) -> Unit,
    onRemoveMissionImageClick: () -> Unit,
    onAddTaskClick: (Task) -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit,
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showStartDateModal by remember { mutableStateOf(false) }
    var showEndDateModal by remember { mutableStateOf(false) }
    var bottomSheetType by remember { mutableStateOf<MissionBottomSheetType?>(null) }
    var showDeleteMissionDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = onImageUriChange
    )

    if (showDeleteMissionDialog) {
        SensibleActionDialog(
            text = stringResource(id = R.string.delete_mission_image_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteMissionDialog = false
                onRemoveMissionImageClick()
            },
            onCancel = { showDeleteMissionDialog = false }
        )
    }

    Scaffold(
        topBar = {
            EditTopBar(
                title = stringResource(R.string.new_mission),
                onCancelClick = onBackClick,
                onActionClick = onCreateMissionClick,
                actionLabel = stringResource(com.upsaclay.common.R.string.publish),
                isButtonEnable = createEnabled
            )
        }
    ) { innerPadding ->
        CreateMissionForm(
            modifier = Modifier
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { focusManager.clearFocus() }
                    )
                },
            title = title,
            description = description,
            schoolLevels = schoolLevels,
            selectedSchoolLevels = selectedSchoolLevels,
            startDate = startDate,
            endDate = endDate,
            frequency = frequency,
            selectedManagers = selectedManagers,
            tasks = tasks,
            imageUri = imageUri,
            onTitleChange = onTitleChange,
            onDescriptionChange = onDescriptionChange,
            onSelectedSchoolLevelChange = onSelectedSchoolLevelChange,
            onStartDateClick = { showStartDateModal = true },
            onEndDateClick = { showEndDateModal = true },
            onFrequencyChange = onFrequencyChange,
            onShowManagerListClick = { bottomSheetType = MissionBottomSheetType.SelectManager },
            onRemoveManagerClick = onRemoveManagerClick,
            onAddTaskClick = { bottomSheetType = MissionBottomSheetType.AddTask },
            onEditTaskClick = { bottomSheetType = MissionBottomSheetType.EditTask(it) },
            onRemoveTaskClick = onRemoveTaskClick,
            onImageClick = { bottomSheetType = MissionBottomSheetType.ModifyImage },
            onRemoveImageClick = onRemoveMissionImageClick
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
            (bottomSheetType as? MissionBottomSheetType.EditTask)?.task?.let {
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
                selectedManagers = selectedManagers,
                userQuery = userQuery,
                onUserQueryChange = onUserQueryChange,
                onResetQuery = onResetUserQuery,
                onSaveClick = {
                    onSaveSelectedMangers(it)
                    bottomSheetType = null
                },
                onDismissRequest = {
                    onResetUserQuery()
                    bottomSheetType = null
                }
            )
        }

        is MissionBottomSheetType.ModifyImage -> {
            ImageModalBottomSheet(
                onNewProfilePictureClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
                showDeleteImage = imageUri != null,
                onDeleteClick = { showDeleteMissionDialog = true },
                onDismiss = { bottomSheetType = null }
            )
        }

        else -> Unit
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
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun CreateMissionScreenPreview() {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    var currentTask by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(tasksFixture) }

    GedoiseTheme {
        Surface {
            CreateMissionScreen(
                title = title,
                description = description,
                schoolLevels = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2),
                selectedSchoolLevels = emptyList(),
                startDate = LocalDate.now(),
                endDate = LocalDate.now(),
                frequency = frequency,
                selectedManagers = listOf(userFixture),
                tasks = tasks,
                imageUri = null,
                users = usersFixture,
                userQuery = "",
                createEnabled = false,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onSelectedSchoolLevelChange = {},
                onRemoveManagerClick = {},
                onStartDateChange = {},
                onEndDateChange = {},
                onFrequencyChange = { frequency = it },
                onSaveSelectedMangers = {},
                onUserQueryChange = {},
                onResetUserQuery = {},
                onImageUriChange = {},
                onRemoveMissionImageClick = {},
                onAddTaskClick = {
                    tasks = tasks + it
                    currentTask = ""
                },
                onEditTaskClick = {},
                onRemoveTaskClick = { tasks = tasks - it },
                onCreateMissionClick = {},
                onBackClick = {},
            )
        }
    }
}