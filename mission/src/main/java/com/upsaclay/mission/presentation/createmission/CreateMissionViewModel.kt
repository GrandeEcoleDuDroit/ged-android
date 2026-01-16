package com.upsaclay.mission.presentation.createmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.extensions.replace
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_DESCRIPTION_LENGTH
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_DURATION_LENGTH
import com.upsaclay.mission.presentation.MissionPresentationUtils.MAX_TITLE_LENGTH
import com.upsaclay.mission.presentation.extension.missionManagerSorting
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
    private val getUsersUseCase: GetUsersUseCase,
    private val generateIdUseCase: GenerateIdUseCase
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
            id = generateIdUseCase(),
            title = uiState.value.title.trim(),
            description = uiState.value.description.trim(),
            date = LocalDateTime.now(ZoneOffset.UTC),
            startDate = uiState.value.startDate,
            endDate = uiState.value.endDate,
            schoolLevels = uiState.value.schoolLevels,
            duration = uiState.value.duration.takeIf { it.isNotBlank() }?.trim(),
            managers = uiState.value.managers,
            participants = emptyList(),
            maxParticipants = uiState.value.maxParticipants.trim().toInt(),
            tasks = uiState.value.missionTasks,
            state = MissionState.Draft,
        )

        createMissionUseCase(mission, uiState.value.imageUri?.toString())
    }

    fun onImageUriChange(uri: Uri?) {
        _uiState.update {
            it.copy(imageUri = uri)
        }
    }

    fun onRemoveImageUri() {
        _uiState.update {
            it.copy(imageUri = null)
        }
    }

    fun onTitleChange(title: String) {
        val truncatedTitle = title.take(MAX_TITLE_LENGTH)
        _uiState.update {
            it.copy(
                title = truncatedTitle,
                createEnabled = validateCreate(title = truncatedTitle)
            )
        }
    }

    fun onDescriptionChange(description: String) {
        val truncatedDescription = description.take(MAX_DESCRIPTION_LENGTH)
        _uiState.update {
            it.copy(
                description = truncatedDescription,
                createEnabled = validateCreate(description = truncatedDescription)
            )
        }
    }

    fun onStartDateChange(startDate: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = startDate,
                endDate = if (!validateEndDate(startDate, it.endDate)) startDate else it.endDate
            )
        }
    }

    fun onEndDateChange(endDate: LocalDate) {
        _uiState.update {
            it.copy(
                startDate = if (!validateEndDate(it.startDate, endDate)) endDate else it.startDate,
                endDate = endDate
            )
        }
    }

    fun onSchoolLevelChange(schoolLevel: SchoolLevel) {
        _uiState.update {
            val schoolLevels = if (it.schoolLevels.contains(schoolLevel)) {
                it.schoolLevels - schoolLevel
            } else {
                it.schoolLevels + schoolLevel
            }.sorted()

            it.copy(schoolLevels = schoolLevels)
        }
    }

    fun onMaxParticipantsChange(maxParticipants: String) {
        val value = when {
            maxParticipants.isEmpty() -> ""
            maxParticipants.toIntOrNull()?.let { it > 0 } == true -> maxParticipants.toInt().toString()
            else -> uiState.value.maxParticipants
        }

        _uiState.update {
            it.copy(
                maxParticipants = value,
                createEnabled = validateCreate(maxParticipants = value)
            )
        }
    }

    fun onDurationChange(duration: String) {
        val truncatedDuration = duration.take(MAX_DURATION_LENGTH)
        _uiState.update {
            it.copy(duration = truncatedDuration)
        }
    }

    fun onSaveManagers(managers: List<User>) {
        _uiState.update {
            it.copy(managers = managers)
        }
    }

    fun onRemoveManager(manager: User) {
        val managers = uiState.value.managers

        if (managers.size > 1) {
            _uiState.update {
                it.copy(managers = managers - manager)
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
        _uiState.update {
            it.copy(missionTasks = it.missionTasks + missionTask)
        }
    }

    fun onEditMissionTask(missionTask: MissionTask) {
        _uiState.update { state ->
            state.copy(
                missionTasks = state.missionTasks.replace(
                    predicate = { it.id == missionTask.id },
                    value = missionTask.copy(value = missionTask.value.trim())
                )
            )
        }
    }

    fun onRemoveMissionTask(missionTask: MissionTask) {
        _uiState.update { state ->
            state.copy(
                missionTasks = state.missionTasks - missionTask
            )
        }
    }

    private fun initCurrentUser() {
        viewModelScope.launch {
            userRepository.user.take(1).collect { user ->
                _uiState.update {
                    it.copy(
                        user = user,
                        managers = listOf(user),
                    )
                }
            }
        }
    }

    private fun initUsers() {
        viewModelScope.launch {
            getUsersUseCase()
                .missionManagerSorting()
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
        return validateTitle(title) &&
                validateDescription(description) &&
                validateMaxParticipants(maxParticipants)
    }

    private fun validateTitle(title: String): Boolean = title.isNotBlank()

    private fun validateDescription(description: String): Boolean = description.isNotBlank()

    private fun validateEndDate(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun validateMaxParticipants(maxParticipants: String): Boolean = maxParticipants.isNotBlank()

    data class CreateMissionUiState(
        val title: String = "",
        val description: String = "",
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val schoolLevels: List<SchoolLevel> = emptyList(),
        val duration: String = "",
        val managers: List<User> = emptyList(),
        val maxParticipants: String = "",
        val missionTasks: List<MissionTask> = emptyList(),
        val imageUri: Uri? = null,
        val users: List<User> = emptyList(),
        val userQuery: String = "",
        val user: User? = null,
        val createEnabled: Boolean = false
    ) {
        val allSchoolLevels: List<SchoolLevel> = SchoolLevel.all
    }
}