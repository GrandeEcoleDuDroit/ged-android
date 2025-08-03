package com.upsaclay.forum.presentation.createmission

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.forum.domain.entity.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class CreateMissionViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateMissionUiState())
    val uiState: StateFlow<CreateMissionUiState> = _uiState
    private var defaultUsers: List<User> = emptyList()

    init {
        initUser()
        initUsers()
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

    fun onFrequencyChange(frequency: String) {
        if (frequency.length > 100) return
        _uiState.update {
            it.copy(
                frequency = frequency,
                createEnabled = validateCreate(frequency = frequency)
            )
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

    fun onMissionImageUriChange(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun onRemoveImageUri() {
        _uiState.update { it.copy(imageUri = null) }
    }

    fun onAddTask(task: Task) {
        _uiState.update {
            it.copy(
                tasks = it.tasks + (task.id to task),
                currentTask = ""
            )
        }
    }

    fun onEditTask(task: Task) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks + (task.id to task)
            )
        }
    }

    fun onRemoveTask(task: Task) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks - task.id
            )
        }
    }

    fun onUserQueryChange(query: String) {
        _uiState.update {
            it.copy(userQuery = query)
        }

        getFilteredUsers(query).also { users ->
            _uiState.update {
                it.copy(users = users)
            }
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

    private fun getFilteredUsers(query: String): List<User> {
        return query.takeIf { it.isNotBlank() }?.let {
            defaultUsers.filter { user ->
                user.firstName.contains(query, ignoreCase = true) ||
                        user.lastName.contains(query, ignoreCase = true)
            }
        } ?: defaultUsers
    }

    private fun initUser() {
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
            userRepository.getUsers()
                .also { users ->
                    _uiState.update { it.copy(users = users) }
                    defaultUsers = users
                }
        }
    }

    private fun validateCreate(
        title: String = _uiState.value.title,
        description: String = _uiState.value.description,
        frequency: String = _uiState.value.frequency
    ): Boolean {
        return validateTitle(title) &&
                validateDescription(description) &&
                validateFrequency(frequency)
    }

    private fun validateTitle(title: String): Boolean = title.isNotBlank()

    private fun validateDescription(description: String): Boolean = description.isNotBlank()

    private fun validateEndDate(startDate: LocalDate, endDate: LocalDate): Boolean =
        endDate.isEqual(startDate) || endDate.isAfter(startDate)

    private fun validateFrequency(frequency: String): Boolean = frequency.isNotBlank()

    data class CreateMissionUiState(
        val title: String = "",
        val description: String = "",
        val selectedSchoolLevels: List<SchoolLevel> = emptyList(),
        val schoolLevels: List<SchoolLevel> = SchoolLevel.entries,
        val startDate: LocalDate = LocalDate.now(),
        val endDate: LocalDate = LocalDate.now(),
        val frequency: String = "",
        val selectedManagers: List<User> = emptyList(),
        val currentTask: String = "",
        val tasks: Map<Int, Task> = emptyMap(),
        val imageUri: Uri? = null,
        val users: List<User> = emptyList(),
        val userQuery: String = "",
        val user: User? = null,
        val createEnabled: Boolean = false,
    )
}