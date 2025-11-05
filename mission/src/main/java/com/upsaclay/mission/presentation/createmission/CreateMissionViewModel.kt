package com.upsaclay.mission.presentation.createmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class CreateMissionViewModel(
    private val userRepository: UserRepository,
    private val createMissionUseCase: CreateMissionUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateMissionUiState())
    val uiState: StateFlow<CreateMissionUiState> = _uiState
    private var defaultUsers: List<User> = emptyList()

    init {
        initCurrentUser()
        initUsers()
    }

    fun createMission() {
        val mission = Mission(
            id = GenerateIdUseCase.intId,
            title = uiState.value.title,
            description = uiState.value.description,
            schoolLevels = uiState.value.selectedSchoolLevels,
            date = LocalDateTime.now(ZoneOffset.UTC),
            startDate = uiState.value.startDate,
            endDate = uiState.value.endDate,
            duration = uiState.value.duration.takeIf { it.isNotBlank() },
            managers = uiState.value.selectedManagers,
            participants = emptyList(),
            maxParticipants = uiState.value.maxParticipants.toInt(),
            tasks = uiState.value.tasks.values.toList(),
            state = MissionState.Draft(imageUri = uiState.value.imageUri?.toString()),
        )

        createMissionUseCase(mission)
    }

    fun onImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onRemoveImageUri() {
        _uiState.update { it.copy(imageUri = null) }
    }

    fun onTitleChange(title: String) {
        val titleTruncated = title.take(100)
        _uiState.update {
            it.copy(
                title = titleTruncated,
                createEnabled = validateCreate(title = titleTruncated)
            )
        }
    }

    fun onDescriptionChange(description: String) {
        val descriptionTruncated = description.take(1000)
        _uiState.update {
            it.copy(
                description = descriptionTruncated,
                createEnabled = validateCreate(description = descriptionTruncated)
            )
        }
    }

    fun onSelectedSchoolLevelChange(schoolLevel: SchoolLevel) {
        _uiState.update { currentState ->
            val updatedSchoolLevels = if (currentState.selectedSchoolLevels.contains(schoolLevel)) {
                currentState.selectedSchoolLevels - schoolLevel
            } else {
                currentState.selectedSchoolLevels + schoolLevel
            }
            currentState.copy(selectedSchoolLevels = updatedSchoolLevels.sorted())
        }
    }

    fun onStartDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = date,
                endDate = if (!endDateValid(date, it.endDate)) date else it.endDate,
            )
        }
    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = if (!endDateValid(it.startDate, date)) date else it.startDate,
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

    fun onMaxParticipantsChange(maxParticipants: String) {
        if (maxParticipants.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    maxParticipants = maxParticipants,
                    createEnabled = validateCreate(maxParticipants = maxParticipants)
                )
            }
        }
    }

    fun onSaveSelectedManagers(selectedManagers: List<User>) {
        _uiState.update { it.copy(selectedManagers = selectedManagers) }
    }

    fun onRemoveManager(manager: User) {
        _uiState.update { currentState ->
            val updatedManagers = currentState.selectedManagers - manager
            currentState.copy(selectedManagers = updatedManagers)
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
        _uiState.update {
            it.copy(
                tasks = it.tasks + (missionTask.id to missionTask),
                currentTask = ""
            )
        }
    }

    fun onEditTask(missionTask: MissionTask) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks + (missionTask.id to missionTask)
            )
        }
    }

    fun onRemoveTask(missionTask: MissionTask) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks - missionTask.id
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

    private fun initCurrentUser() {
        viewModelScope.launch {
            userRepository.user.take(1).collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        selectedManagers = listOf(user),
                    )
                }
            }
        }
    }

    private fun initUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .sortedByDescending { it.admin }
                .also { users ->
                    _uiState.update { it.copy(users = users) }
                    defaultUsers = users
                }
        }
    }

    private fun validateCreate(
        title: String = uiState.value.title,
        description: String = uiState.value.description,
        maxParticipants: String = uiState.value.maxParticipants
    ): Boolean {
        return titleValid(title) &&
                descriptionValid(description) &&
                maxParticipantsValid(maxParticipants)
    }

    private fun titleValid(title: String): Boolean = title.isNotBlank()

    private fun descriptionValid(description: String): Boolean = description.isNotBlank()

    private fun endDateValid(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun maxParticipantsValid(maxParticipants: String): Boolean =
        maxParticipants.toIntOrNull()?.let { it > 0 } ?: false

    data class CreateMissionUiState(
        val title: String = "",
        val description: String = "",
        val selectedSchoolLevels: List<SchoolLevel> = emptyList(),
        val schoolLevels: List<SchoolLevel> = SchoolLevel.getSchoolLevels(),
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val duration: String = "",
        val selectedManagers: List<User> = emptyList(),
        val maxParticipants: String = "",
        val currentTask: String = "",
        val tasks: Map<Int, MissionTask> = emptyMap(),
        val imageUri: Uri? = null,
        val users: List<User> = emptyList(),
        val userQuery: String = "",
        val user: User? = null,
        val createEnabled: Boolean = false
    )
}