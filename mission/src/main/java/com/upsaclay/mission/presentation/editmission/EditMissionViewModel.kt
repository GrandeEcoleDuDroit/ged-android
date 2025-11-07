package com.upsaclay.mission.presentation.editmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.NoInternetConnectionException
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.replace
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.utils.mapNetworkErrorMessage
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.repository.MissionRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import java.time.LocalDate

class EditMissionViewModel(
    private val mission: Mission,
    private val missionRepository: MissionRepository,
    private val connectivityObserver: ConnectivityObserver,
    private val getUsersUseCase: GetUsersUseCase,
): ViewModel() {
    private val _uiState = MutableStateFlow(
        EditMissionUiState(
            title = mission.title,
            description = mission.description,
            schoolLevels = mission.schoolLevels,
            startDate = mission.startDate,
            endDate = mission.endDate,
            duration = mission.duration.orEmpty(),
            managers = mission.managers,
            participants = mission.participants,
            maxParticipants = mission.maxParticipants.toString(),
            tasks = mission.tasks,
            state = mission.state
        )
    )
    val uiState: StateFlow<EditMissionUiState> = _uiState
    private var defaultUsers: List<User> = emptyList()
    private val _event = MutableSharedFlow<SingleUiEvent>()
    val event: SharedFlow<SingleUiEvent> = _event

    init {
        initUsers()
    }

    fun updateMission() {
        if (!validateEdit()) return

        val trimmedMission = mission.copy(
            title = uiState.value.title.trim(),
            description = uiState.value.description.trim(),
            schoolLevels = uiState.value.schoolLevels,
            startDate = uiState.value.startDate,
            endDate = uiState.value.endDate,
            duration = uiState.value.duration.takeIf { it.isNotBlank() }?.trim(),
            managers = uiState.value.managers,
            maxParticipants = uiState.value.maxParticipants.trim().toInt(),
            tasks = uiState.value.tasks
        )

        viewModelScope.launch {
            try {
                if (!connectivityObserver.isConnected) {
                    throw NoInternetConnectionException()
                }
                _uiState.update {
                    it.copy(loading = true)
                }
                missionRepository.updateMission(trimmedMission)
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

    fun onTitleChange(title: String) {
        val titleTruncated = title.take(100)
        _uiState.update {
            it.copy(
                title = titleTruncated,
                editEnabled = validateEdit(title = titleTruncated)
            )
        }
    }

    fun onDescriptionChange(description: String) {
        val descriptionTruncated = description.take(1000)
        _uiState.update {
            it.copy(
                description = descriptionTruncated,
                editEnabled = validateEdit(description = descriptionTruncated)
            )
        }
    }

    fun onSchoolLevelChange(schoolLevel: SchoolLevel) {
        _uiState.update { currentState ->
            val updatedSchoolLevels = if (currentState.schoolLevels.contains(schoolLevel)) {
                currentState.schoolLevels - schoolLevel
            } else {
                currentState.schoolLevels + schoolLevel
            }
            currentState.copy(schoolLevels = updatedSchoolLevels.sorted())
        }
    }

    fun onStartDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = date,
                endDate = if (!validateEndDate(date, it.endDate)) date else it.endDate,
            )
        }
    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = if (!validateEndDate(it.startDate, date)) date else it.startDate,
                endDate = date
            )
        }
    }

    fun onDurationChange(duration: String) {
        val durationTruncated = duration.take(200)
        _uiState.update {
            it.copy(duration = durationTruncated)
        }
    }

    fun onRemoveParticipant(participant: User) {
        _uiState.update {
            it.copy(participants = it.participants - participant)
        }
    }

    fun onMaxParticipantsChange(maxParticipants: String) {
        if (maxParticipants.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    maxParticipants = maxParticipants,
                    editEnabled = validateEdit(maxParticipants = maxParticipants)
                )
            }
        }
    }

    fun onSaveManagers(managers: List<User>) {
        _uiState.update { it.copy(managers = managers) }
    }

    fun onRemoveManager(manager: User) {
        _uiState.update {
            it.copy(managers = it.managers - manager)
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

    fun onAddTask(missionTask: MissionTask) {
        val trimmedTask = missionTask.copy(value = missionTask.value.trim())
        _uiState.update {
            it.copy(
                tasks = it.tasks + trimmedTask
            )
        }
    }

    fun onEditTask(missionTask: MissionTask) {
        val trimmedTask = missionTask.copy(value = missionTask.value.trim())
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.replace(
                    predicate = { it.id == missionTask.id },
                    value = trimmedTask
                )
            )
        }
    }

    fun onRemoveTask(missionTask: MissionTask) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks - missionTask
            )
        }
    }

    fun onImageUriChange(uri: Uri?) {
        _uiState.update {
            it.copy(
                state = MissionState.Draft(uri?.toString()),
                editEnabled = validateEdit(imageModel = uri?.toString())
            )
        }
    }

    fun onRemoveImageUri() {
        _uiState.update {
            it.copy(
                state = MissionState.Draft(null),
                editEnabled = validateEdit(imageModel = null)
            )
        }
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

    private fun validateEdit(
        title: String = uiState.value.title,
        description: String = uiState.value.description,
        maxParticipants: String = uiState.value.maxParticipants,
        imageModel: String? = uiState.value.state.imageModel
    ): Boolean = (
            validateTitle(title) ||
            validateDescription(description) ||
            validateMaxParticipants(maxParticipants) ||
            validateImageModel(imageModel)
        ) && validateMandatoryFields()

    private fun validateTitle(title: String): Boolean = title != mission.title

    private fun validateDescription(description: String): Boolean = description != mission.description

    private fun validateEndDate(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun validateMaxParticipants(maxParticipants: String): Boolean =
        maxParticipants.toIntOrNull()?.let { it > 0 } ?: false

    private fun validateImageModel(imageModel: String?): Boolean =
        imageModel != mission.state.imageModel

    private fun validateMandatoryFields(): Boolean {
        return uiState.value.title.isNotBlank() &&
                uiState.value.description.isNotBlank() &&
                uiState.value.maxParticipants.isNotBlank()
    }

    data class EditMissionUiState(
        val title: String = "",
        val description: String = "",
        val schoolLevels: List<SchoolLevel> = emptyList(),
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val duration: String = "",
        val managers: List<User> = emptyList(),
        val participants: List<User> = emptyList(),
        val maxParticipants: String = "",
        val tasks: List<MissionTask> = emptyList(),
        val state: MissionState = MissionState.Published(),
        val users: List<User> = emptyList(),
        val userQuery: String = "",
        val editEnabled: Boolean = false,
        val loading: Boolean = false
    ) {
        val allSchoolLevels: List<SchoolLevel> = SchoolLevel.getSchoolLevels()
    }
}