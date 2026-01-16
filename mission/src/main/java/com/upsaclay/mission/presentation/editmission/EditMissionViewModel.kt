package com.upsaclay.mission.presentation.editmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.replace
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapException
import com.upsaclay.mission.R
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.usecase.UpdateMissionUseCase
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_DESCRIPTION_LENGTH
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_DURATION_LENGTH
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_TITLE_LENGTH
import com.upsaclay.mission.presentation.extension.missionManagerSorting
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditMissionViewModel(
    private val mission: Mission,
    private val getUsersUseCase: GetUsersUseCase,
    private val updateMissionUseCase: UpdateMissionUseCase,
    private val generateIdUseCase: GenerateIdUseCase
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
    private val missionUpdateState = MutableStateFlow(MissionUpdateState())

    init {
        initUsers()
        listenMissionUpdateState()
    }

    fun updateMission() {
        val (
            title,
            description,
            startDate,
            endDate,
            schoolLevels,
            duration,
            managers,
            maxParticipants,
            tasks,
            state
        ) = uiState.value

        if (!validateInputs(maxParticipants = maxParticipants)) return

        val newMission = mission.copy(
            title = title.trim(),
            description = description.trim(),
            startDate = startDate,
            endDate = endDate,
            schoolLevels = schoolLevels,
            duration = duration.takeIf { it.isNotBlank() }?.trim(),
            managers = managers,
            maxParticipants = maxParticipants.trim().toInt(),
            tasks = tasks,
            state = state
        )

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(loading = true)
                }
                updateMissionUseCase(
                    mission = newMission,
                    imageUri = uiState.value.imageUri?.toString()
                )
                _event.emit(SingleUiEvent.Success())
            } catch (e: Exception) {
                _event.emit(SingleUiEvent.Error(mapException(e)))
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
        missionUpdateState.update {
            it.copy(imageReferenceUpdated = validateImageReferenceUpdate(uri?.toString()))
        }
    }

    fun onRemoveImage() {
        val state = when (mission.state) {
            is MissionState.Draft -> MissionState.Draft
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
        missionUpdateState.update {
            it.copy(imageReferenceUpdated = validateImageReferenceUpdate(null))
        }
    }

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(title = truncatedTitle)
        }
        missionUpdateState.update {
            it.copy(titleUpdated = validateTitleUpdate(truncatedTitle))
        }
    }

    fun onDescriptionChange(description: String) {
        val truncatedDescription = description.take(MAX_DESCRIPTION_LENGTH)
        _uiState.update {
            it.copy(description = truncatedDescription)
        }
        missionUpdateState.update {
            it.copy(descriptionUpdated = validateDescriptionUpdate(truncatedDescription))
        }
    }

    fun onStartDateChange(startDate: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = if (!validateEndDateUpdate(startDate, it.endDate)) startDate else it.endDate
            )
        }
        missionUpdateState.update {
            it.copy(startDateUpdated = validateStartDateUpdate(startDate))
        }
    }

    fun onEndDateChange(endDate: LocalDate) {
        val endDateValid = validateEndDateUpdate(uiState.value.startDate, endDate)
        _uiState.update {
            it.copy(
                startDate = if (!endDateValid) endDate else it.startDate,
                endDate = endDate
            )
        }
        missionUpdateState.update {
            it.copy(endDateUpdated = endDateValid)
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
        missionUpdateState.update {
            it.copy(schoolLevelsUpdated = validateSchoolLevelsUpdate(schoolLevels))
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
        missionUpdateState.update {
            it.copy(maxParticipantsUpdated = validateMaxParticipantsUpdate(value))
        }
    }

    fun onDurationChange(duration: String) {
        val truncatedDuration = duration.take(MAX_DURATION_LENGTH)
        _uiState.update {
            it.copy(duration = truncatedDuration)
        }
        missionUpdateState.update {
            it.copy(durationUpdated = validateDurationUpdate(truncatedDuration))
        }
    }

    fun onSaveManagers(managers: List<User>) {
        _uiState.update {
            it.copy(managers = managers)
        }
        missionUpdateState.update {
            it.copy(managersUpdated = validateManagersUpdate(managers))
        }
    }

    fun onRemoveManager(manager: User) {
        val managers = uiState.value.managers

        if (managers.size > 1) {
            val updatedManagers = managers - manager
            _uiState.update {
                it.copy(managers = updatedManagers)
            }
            missionUpdateState.update {
                it.copy(managersUpdated = validateManagersUpdate(updatedManagers))
            }
        }
    }

    fun onUserQueryChange(query: String) {
        _uiState.update {
            it.copy(userQuery = query)
        }

        filterUsersByName(query)
    }

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

    fun onResetUserQuery() {
        _uiState.update {
            it.copy(
                userQuery = "",
                users = defaultUsers
            )
        }
    }

    fun onAddMissionTask(missionTaskValue: String) {
        val missionTask = MissionTask(generateIdUseCase(), missionTaskValue.trim())
        val missionTasks = uiState.value.tasks + missionTask
        _uiState.update {
            it.copy(tasks = missionTasks)
        }
        missionUpdateState.update {
            it.copy(missionTasksUpdated = validateMissionTasksUpdate(missionTasks))
        }
    }

    fun onEditMissionTask(missionTask: MissionTask) {
        val tasks = uiState.value.tasks.replace(
            predicate = { it.id == missionTask.id },
            value = missionTask.copy(value = missionTask.value.trim())
        )
        _uiState.update {
            it.copy(tasks = tasks)
        }
        missionUpdateState.update {
            it.copy(missionTasksUpdated = validateMissionTasksUpdate(tasks))
        }
    }

    fun onRemoveMissionTask(missionTask: MissionTask) {
        val tasks = uiState.value.tasks - missionTask
        _uiState.update { state ->
            state.copy(tasks = tasks)
        }
        missionUpdateState.update {
            it.copy(missionTasksUpdated = validateMissionTasksUpdate(tasks))
        }
    }

    private fun validateInputs(maxParticipants: String): Boolean =
        validateMaxParticipantsInput(maxParticipants)

    private fun validateImageReferenceUpdate(imageReference: String?): Boolean =
        imageReference != mission.state.imageReference

    private fun validateTitleUpdate(title: String): Boolean = title != mission.title && title.isNotBlank()

    private fun validateDescriptionUpdate(description: String): Boolean =
        description != mission.description && description.isNotBlank()

    private fun validateSchoolLevelsUpdate(schoolLevels: List<SchoolLevel>): Boolean =
        schoolLevels != mission.schoolLevels

    private fun validateStartDateUpdate(startDate: LocalDate): Boolean =
        startDate != mission.startDate

    private fun validateEndDateUpdate(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate != mission.endDate && (endDate.isEqual(startDate) || endDate.isAfter(startDate))

    private fun validateMaxParticipantsUpdate(maxParticipants: String): Boolean =
        maxParticipants != mission.maxParticipants.toString() && maxParticipants.isNotBlank()

    private fun validateDurationUpdate(duration: String): Boolean =
        duration != mission.duration.orEmpty()

    private fun validateManagersUpdate(managers: List<User>): Boolean =
        managers != mission.managers

    private fun validateMissionTasksUpdate(missionTasks: List<MissionTask>): Boolean =
        missionTasks != mission.tasks

    private fun validateMandatoryFields(): Boolean =
        uiState.value.title.isNotBlank() &&
                uiState.value.description.isNotBlank() &&
                uiState.value.maxParticipants.isNotBlank()

    private fun validateMaxParticipantsInput(maxParticipants: String): Boolean {
        val maxParticipantsNumber = maxParticipants.toIntOrNull()
        val maxParticipantsError = when {
            maxParticipants.isBlank() -> com.upsaclay.common.R.string.mandatory_field_error
            maxParticipantsNumber == null -> com.upsaclay.common.R.string.mandatory_field_error
            maxParticipantsNumber < mission.participants.size -> R.string.max_participants_lower_than_current_error
            else -> null
        }

        _uiState.update {
            it.copy(maxParticipantsError = maxParticipantsError)
        }

        return uiState.value.maxParticipantsError == null
    }

    private fun listenMissionUpdateState() {
        viewModelScope.launch {
            missionUpdateState.collect { missionUpdateState ->
                _uiState.update {
                    it.copy(updateEnabled = missionUpdateState.updated && validateMandatoryFields())
                }
            }
        }
    }

    private fun initUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .missionManagerSorting(mission.managers)
                .also { users ->
                    _uiState.update { it.copy(users = users) }
                    defaultUsers = users
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
        val updateEnabled: Boolean = false,
        val maxParticipantsError: Int? = null
    ) {
        val allSchoolLevels: List<SchoolLevel> = SchoolLevel.all
    }

    private data class MissionUpdateState(
        val titleUpdated: Boolean = false,
        val descriptionUpdated: Boolean = false,
        val startDateUpdated: Boolean = false,
        val endDateUpdated: Boolean = false,
        val schoolLevelsUpdated: Boolean = false,
        val maxParticipantsUpdated: Boolean = false,
        val durationUpdated: Boolean = false,
        val managersUpdated: Boolean = false,
        val missionTasksUpdated: Boolean = false,
        val imageReferenceUpdated: Boolean = false
    ) {
        val updated: Boolean
            get() = titleUpdated ||
                    descriptionUpdated ||
                    startDateUpdated ||
                    endDateUpdated ||
                    schoolLevelsUpdated ||
                    maxParticipantsUpdated ||
                    durationUpdated ||
                    managersUpdated ||
                    missionTasksUpdated ||
                    imageReferenceUpdated
    }
}