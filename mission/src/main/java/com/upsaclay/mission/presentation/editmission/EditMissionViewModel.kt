package com.upsaclay.mission.presentation.editmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.replace
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.mission.domain.MissionConstants.MAX_DESCRIPTION_LENGTH
import com.upsaclay.mission.domain.MissionConstants.MAX_DURATION_LENGTH
import com.upsaclay.mission.domain.MissionConstants.MAX_TITLE_LENGTH
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.usecase.UpdateMissionUseCase
import com.upsaclay.mission.presentation.extension.managerSorting
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditMissionViewModel(
    private val mission: Mission,
    private val connectivityObserver: ConnectivityObserver,
    private val getUsersUseCase: GetUsersUseCase,
    private val updateMissionUseCase: UpdateMissionUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(
        EditMissionUiState(
            title = mission.title,
            description = mission.description,
            startDate = mission.startDate,
            endDate = mission.endDate,
            schoolLevels = mission.schoolLevels,
            duration = mission.duration.orEmpty(),
            managers = mission.managers,
            maxParticipants = mission.maxParticipants.toString(),
            tasks = mission.tasks,
            state = mission.state
        )
    )
    val uiState: StateFlow<EditMissionUiState> = _uiState
    private var defaultUsers: List<User> = emptyList()
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event
    private val missionUpdate = MutableStateFlow(MissionUpdate())

    init {
        initUsers()
        listenMissionUpdate()
    }

    fun updateMission() {
        if (!uiState.value.updateEnabled) return

        val newMission = mission.copy(
            title = uiState.value.title.trim(),
            description = uiState.value.description.trim(),
            startDate = uiState.value.startDate,
            endDate = uiState.value.endDate,
            schoolLevels = uiState.value.schoolLevels,
            duration = uiState.value.duration.takeIf { it.isNotBlank() }?.trim(),
            managers = uiState.value.managers,
            maxParticipants = uiState.value.maxParticipants.trim().toInt(),
            tasks = uiState.value.tasks,
            state = uiState.value.state
        )

        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                updateMissionUseCase(
                    mission = newMission,
                    newImageUri = uiState.value.imageUri?.toString(),
                    oldMissionState = mission.state
                )
                _event.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapNetworkErrorMessage(e)))
            } finally {
                _uiState.update {
                    it.copy(loading = false)
                }
            }
        }
    }

    fun onImageUriChange(uri: Uri?) {
        _uiState.update {
            it.copy(imageUri = uri)
        }
        missionUpdate.update {
            it.copy(imageModelUpdated = validateImageModel(uri?.toString()))
        }
    }

    fun onRemoveImage() {
        val state = when (mission.state) {
            is MissionState.Draft -> MissionState.Draft(null)
            is MissionState.Publishing -> MissionState.Publishing(null)
            is MissionState.Published -> MissionState.Published(null)
            is MissionState.Error -> MissionState.Error(null)
        }

        _uiState.update {
            it.copy(
                state = state,
                imageUri = null
            )
        }
        missionUpdate.update {
            it.copy(imageModelUpdated = validateImageModel(null))
        }
    }

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(title = truncatedTitle)
        }
        missionUpdate.update {
            it.copy(titleUpdated = validateTitle(truncatedTitle))
        }
    }

    fun onDescriptionChange(description: String) {
        val truncatedDescription = description.take(MAX_DESCRIPTION_LENGTH)
        _uiState.update {
            it.copy(description = truncatedDescription)
        }
        missionUpdate.update {
            it.copy(descriptionUpdated = validateDescription(truncatedDescription))
        }
    }

    fun onStartDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = date,
                endDate = if (!validateEndDate(date, it.endDate)) date else it.endDate
            )
        }
        missionUpdate.update {
            it.copy(startDateUpdated = startDateUpdated(date))
        }
    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = if (!validateEndDate(it.startDate, date)) date else it.startDate,
                endDate = date
            )
        }
        missionUpdate.update {
            it.copy(endDateUpdated = endDateUpdated(date))
        }
    }

    fun onSchoolLevelChange(schoolLevel: SchoolLevel) {
        val currentSchoolLevels = uiState.value.schoolLevels
        val schoolLevels = if (currentSchoolLevels.contains(schoolLevel)) {
            currentSchoolLevels - schoolLevel
        } else {
            currentSchoolLevels + schoolLevel
        }.sorted()

        _uiState.update {
            it.copy(schoolLevels = schoolLevels)
        }
        missionUpdate.update {
            it.copy(schoolLevelsUpdated = validateSchoolLevels(schoolLevels))
        }
    }

    fun onMaxParticipantsChange(maxParticipants: String) {
        val value = when {
            maxParticipants.isEmpty() -> ""
            maxParticipants.toIntOrNull()?.let { it > 0 } == true -> maxParticipants.toInt().toString()
            else -> uiState.value.maxParticipants
        }

        _uiState.update {
            it.copy(maxParticipants = value)
        }
        missionUpdate.update {
            it.copy(maxParticipantsUpdated = validateMaxParticipants(value))
        }
    }

    fun onDurationChange(duration: String) {
        val truncatedDuration = duration.take(MAX_DURATION_LENGTH)
        _uiState.update {
            it.copy(duration = truncatedDuration)
        }
        missionUpdate.update {
            it.copy(durationUpdated = validateDuration(truncatedDuration))
        }
    }

    fun onSaveManagers(managers: List<User>) {
        val sortedManagers = managers.managerSorting()
        _uiState.update {
            it.copy(managers = sortedManagers)
        }
        missionUpdate.update {
            it.copy(managersUpdated = validateManagers(sortedManagers))
        }
    }

    fun onRemoveManager(manager: User) {
        val managers = uiState.value.managers

        if (
            managers.size > 1 ||
            !managers.contains(manager)
        ) {
            val updatedManagers = managers - manager
            _uiState.update {
                it.copy(managers = updatedManagers)
            }
            missionUpdate.update {
                it.copy(managersUpdated = validateManagers(updatedManagers))
            }
        }
    }

    fun onUserQueryChange(query: String) {
        _uiState.update {
            it.copy(userQuery = query)
        }

        filterUsersByName(query)
    }

    fun onResetUserQuery() {
        _uiState.update {
            it.copy(
                userQuery = "",
                users = defaultUsers
            )
        }
    }

    fun onAddTask(value: String) {
        val task = MissionTask(GenerateIdUseCase(), value)
        val tasks = uiState.value.tasks + task
        _uiState.update {
            it.copy(tasks = tasks)
        }
        missionUpdate.update {
            it.copy(tasksUpdated = validateTasks(tasks))
        }
    }

    fun onEditTask(missionTask: MissionTask) {
        val tasks = uiState.value.tasks.replace(
            predicate = { it.id == missionTask.id },
            value = missionTask
        )
        _uiState.update {
            it.copy(tasks = tasks)
        }
        missionUpdate.update {
            it.copy(tasksUpdated = validateTasks(tasks))
        }
    }

    fun onRemoveTask(missionTask: MissionTask) {
        val tasks = uiState.value.tasks - missionTask
        _uiState.update { state ->
            state.copy(tasks = tasks)
        }
        missionUpdate.update {
            it.copy(tasksUpdated = validateTasks(tasks))
        }
    }

    private fun validateImageModel(imageModel: String?): Boolean =
        imageModel != mission.state.imageReference

    private fun validateTitle(title: String): Boolean =
        title != mission.title

    private fun validateDescription(description: String): Boolean =
        description != mission.description

    private fun validateSchoolLevels(schoolLevels: List<SchoolLevel>): Boolean =
        schoolLevels != mission.schoolLevels

    private fun startDateUpdated(startDate: LocalDate): Boolean =
        startDate != mission.startDate

    private fun endDateUpdated(endDate: LocalDate): Boolean =
        endDate != mission.endDate

    private fun validateEndDate(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun validateMaxParticipants(maxParticipants: String): Boolean =
        maxParticipants != mission.maxParticipants.toString()

    private fun validateDuration(duration: String): Boolean =
        duration != mission.duration.orEmpty()

    private fun validateManagers(managers: List<User>): Boolean =
        managers != mission.managers

    private fun validateTasks(tasks: List<MissionTask>): Boolean =
        tasks != mission.tasks

    private fun validateMandatoryFields(): Boolean =
        uiState.value.title.isNotBlank() &&
                uiState.value.description.isNotBlank() &&
                uiState.value.maxParticipants.isNotBlank()

    private fun filterUsersByName(query: String) {
        val users = if (query.isNotBlank()) {
            defaultUsers.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        } else {
            defaultUsers
        }

        _uiState.update {
            it.copy(users = users)
        }
    }

    private fun initUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .sortedBy { it.fullName }
                .sortedByDescending { mission.managers.contains(it) }
                .sortedByDescending { it.admin }
                .also { users ->
                    _uiState.update { it.copy(users = users) }
                    defaultUsers = users
                }
        }
    }

    private fun listenMissionUpdate() {
        viewModelScope.launch {
            missionUpdate.collect { missionUpdate ->
                _uiState.update {
                    it.copy(updateEnabled = missionUpdate.isUpdated && validateMandatoryFields())
                }
            }
        }
    }

    data class EditMissionUiState(
        val title: String = "",
        val description: String = "",
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val schoolLevels: List<SchoolLevel> = emptyList(),
        val duration: String = "",
        val managers: List<User> = emptyList(),
        val maxParticipants: String = "",
        val tasks: List<MissionTask> = emptyList(),
        val state: MissionState = MissionState.Published(),
        val users: List<User> = emptyList(),
        val userQuery: String = "",
        val imageUri: Uri? = null,
        val loading: Boolean = false,
        val updateEnabled: Boolean = false
    ) {
        val allSchoolLevels: List<SchoolLevel> = SchoolLevel.getSchoolLevels()
    }

    private data class MissionUpdate(
        val titleUpdated: Boolean = false,
        val descriptionUpdated: Boolean = false,
        val startDateUpdated: Boolean = false,
        val endDateUpdated: Boolean = false,
        val schoolLevelsUpdated: Boolean = false,
        val maxParticipantsUpdated: Boolean = false,
        val durationUpdated: Boolean = false,
        val managersUpdated: Boolean = false,
        val tasksUpdated: Boolean = false,
        val imageModelUpdated: Boolean = false
    ) {
        val isUpdated: Boolean
            get() = titleUpdated ||
                    descriptionUpdated ||
                    startDateUpdated ||
                    endDateUpdated ||
                    schoolLevelsUpdated ||
                    maxParticipantsUpdated ||
                    durationUpdated ||
                    managersUpdated ||
                    tasksUpdated ||
                    imageModelUpdated
    }
}