package com.upsaclay.mission.presentation.createmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
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
    private val createMissionUseCase: CreateMissionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateMissionUiState())
    val uiState: StateFlow<CreateMissionUiState> = _uiState
    private var defaultMemberUsers: List<User> = emptyList()

    init {
        initCurrentUser()
        initMemberUsers()
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
            frequency = uiState.value.frequency,
            managers = uiState.value.selectedManagers,
            participants = emptyList(),
            maxParticipants = uiState.value.participantNumber.toInt(),
            missionTasks = uiState.value.tasks.values.toList(),
            state = MissionState.Draft(imageUri = uiState.value.imageUri?.toString()),
        )

        createMissionUseCase(mission)
    }

    fun onTitleChange(title: String) {
        if (title.length > 100) return
        _uiState.update {
            it.copy(
                title = title,
                createEnabled = validateCreate(title = title)
            )
        }
    }

    fun onDescriptionChange(description: String) {
        if (description.length > 500) return
        _uiState.update {
            it.copy(
                description = description,
                createEnabled = validateCreate(description = description)
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
                endDate = if (!isEndDateValid(date, it.endDate)) date else it.endDate,
            )
        }
    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = if (!isEndDateValid(it.startDate, date)) date else it.startDate,
                endDate = date
            )
        }
    }

    fun onFrequencyChange(frequency: String) {
        if (frequency.length > 100) return
        _uiState.update {
            it.copy(
                frequency = frequency,
                createEnabled = validateCreate(frequency = frequency)
            )
        }
    }

    fun onParticipantNumberChange(maxParticipants: String) {
        if (maxParticipants.all { it.isDigit() }) {
            _uiState.update {
                it.copy(
                    participantNumber = maxParticipants,
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

    fun onImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onRemoveImageUri() {
        _uiState.update { it.copy(imageUri = null) }
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

    fun onUserQueryChange(query: String) {
        _uiState.update {
            it.copy(userQuery = query)
        }

        filterAdminUsersByName(query)
    }

    fun onResetUserQuery() {
        _uiState.update {
            it.copy(
                userQuery = "",
                memberUsers = defaultMemberUsers
            )
        }
    }

    private fun filterAdminUsersByName(query: String) {
        val users = if (query.isNotBlank()) {
            defaultMemberUsers.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        } else {
            defaultMemberUsers
        }

        _uiState.update {
            it.copy(memberUsers = users)
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

    private fun initMemberUsers() {
        viewModelScope.launch {
            userRepository.getMemberUsers()
                .filterNot { it.isDeleted }
                .also { users ->
                    _uiState.update { it.copy(memberUsers = users) }
                    defaultMemberUsers = users
                }
        }
    }

    private fun validateCreate(
        title: String = uiState.value.title,
        description: String = uiState.value.description,
        frequency: String = uiState.value.frequency,
        maxParticipants: String = uiState.value.participantNumber
    ): Boolean {
        return isTitleValid(title) &&
                isDescriptionValid(description) &&
                isFrequencyValid(frequency) &&
                isMaxParticipantValid(maxParticipants)
    }

    private fun isTitleValid(title: String): Boolean = title.isNotBlank()

    private fun isDescriptionValid(description: String): Boolean = description.isNotBlank()

    private fun isEndDateValid(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun isFrequencyValid(frequency: String): Boolean = frequency.isNotBlank()

    private fun isMaxParticipantValid(maxParticipants: String): Boolean =
        maxParticipants.toIntOrNull()?.let { it > 0 } ?: false

    data class CreateMissionUiState(
        val title: String = "",
        val description: String = "",
        val selectedSchoolLevels: List<SchoolLevel> = emptyList(),
        val schoolLevels: List<SchoolLevel> = SchoolLevel.entries,
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val frequency: String = "",
        val selectedManagers: List<User> = emptyList(),
        val participantNumber: String = "",
        val currentTask: String = "",
        val tasks: Map<Int, MissionTask> = emptyMap(),
        val imageUri: Uri? = null,
        val memberUsers: List<User> = emptyList(),
        val userQuery: String = "",
        val user: User? = null,
        val createEnabled: Boolean = false
    )
}