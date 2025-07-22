package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.common.presentation.components.DatePickerModal
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.Phones
import com.upsaclay.forum.R
import com.upsaclay.forum.domain.entity.Task
import com.upsaclay.forum.domain.tasksFixture
import com.upsaclay.forum.presentation.components.AddTaskBottomSheet
import com.upsaclay.forum.presentation.components.SelectManagerModalBottomSheet
import com.upsaclay.forum.presentation.components.EditTaskBottomSheet
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

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
        tasks = uiState.tasks,
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
        onUserQueryChange = viewModel::onUserQueryChange,
        onResetUserQuery = viewModel::onResetUserQuery,
        onImageUriChange = viewModel::onMissionImageUriChange,
        onRemoveImageUriClick = viewModel::onRemoveImageUri,
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
    startDate: LocalDateTime,
    endDate: LocalDateTime,
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
    onStartDateChange: (LocalDateTime) -> Unit,
    onEndDateChange: (LocalDateTime) -> Unit,
    onFrequencyChange: (String) -> Unit,
    onSaveSelectedMangers: (List<User>) -> Unit,
    onUserQueryChange: (String) -> Unit,
    onResetUserQuery: () -> Unit,
    onImageUriChange: (Uri?) -> Unit,
    onRemoveImageUriClick: () -> Unit,
    onAddTaskClick: (Task) -> Unit,
    onEditTaskClick: (Task) -> Unit,
    onRemoveTaskClick: (Task) -> Unit,
    onCreateMissionClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var showStartDateModal by remember { mutableStateOf(false) }
    var showEndDateModal by remember { mutableStateOf(false) }
    var showSelectManagerBottomSheet by remember { mutableStateOf(false) }
    var showAddTaskBottomSheet by remember { mutableStateOf(false) }
    var showEditTaskBottomSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let(onImageUriChange) }
    )

    fun showSnackBar(message: String, actionLabel: String, actionPerformed: () -> Unit) {
        scope.launch {
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                actionPerformed()
            }
        }
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
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
               Snackbar(it)
            }
        }
    ) { innerPadding ->
        CreateMissionForm(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = MaterialTheme.spacing.medium),
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
            onShowManagerListClick = { showSelectManagerBottomSheet = true },
            onAddTaskClick = { showAddTaskBottomSheet = true },
            onEditTaskClick = {
                selectedTask = it
                showEditTaskBottomSheet = true
             },
            onRemoveTaskClick = {
                onRemoveTaskClick(it)
                showSnackBar(
                    message = context.getString(R.string.task_removed),
                    actionLabel = context.getString(R.string.undo),
                    actionPerformed = { onAddTaskClick(it) }
                )
            },
            onImageClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
            onRemoveImageClick = onRemoveImageUriClick
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

    if (showSelectManagerBottomSheet) {
        SelectManagerModalBottomSheet(
            users = users,
            selectedManagers = selectedManagers,
            userQuery = userQuery,
            onUserQueryChange = onUserQueryChange,
            onResetQuery = onResetUserQuery,
            onSaveClick = {
                onSaveSelectedMangers(it)
                showSelectManagerBottomSheet = false
            },
            onDismiss = {
                onResetUserQuery()
                showSelectManagerBottomSheet = false
            }
        )
    }

    if (showEditTaskBottomSheet) {
        selectedTask?.let {
            EditTaskBottomSheet(
                initialTask = it,
                onDismissRequest = { showEditTaskBottomSheet = false },
                onEditClick = { task ->
                    onEditTaskClick(task)
                    showEditTaskBottomSheet = false
                }
            )
        }
    }

    if (showAddTaskBottomSheet) {
        AddTaskBottomSheet(
            onDismissRequest = { showAddTaskBottomSheet = false },
            onAddClick = { task ->
                onAddTaskClick(task)
                showAddTaskBottomSheet = false
            }
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
                startDate = LocalDateTime.now(),
                endDate = LocalDateTime.now(),
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
                onStartDateChange = {},
                onEndDateChange = {},
                onFrequencyChange = { frequency = it },
                onSaveSelectedMangers = {},
                onUserQueryChange = {},
                onResetUserQuery = {},
                onImageUriChange = {},
                onRemoveImageUriClick = {},
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