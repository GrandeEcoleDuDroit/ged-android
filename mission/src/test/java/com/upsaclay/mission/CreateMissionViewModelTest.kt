package com.upsaclay.mission

import android.net.Uri
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.usecase.GenerateIdUseCase
import com.upsaclay.common.domain.usecase.GetUsersUseCase
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.usersFixture
import com.upsaclay.mission.domain.entity.MissionTask
import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import com.upsaclay.mission.presentation.createmission.CreateMissionViewModel
import com.upsaclay.mission.presentation.extension.missionManagerSorting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateMissionViewModelTest {
    private val userRepository: UserRepository = mockk()
    private val createMissionUseCase: CreateMissionUseCase = mockk()
    private val getUsersUseCase: GetUsersUseCase = mockk()
    private val generateIdUseCase: GenerateIdUseCase = mockk()

    private lateinit var viewModel: CreateMissionViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val newId = "newId"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { generateIdUseCase() } returns newId
        every { userRepository.user } returns MutableStateFlow(userFixture)
        coEvery { createMissionUseCase(any(), any()) } returns Unit
        coEvery { getUsersUseCase() } returns usersFixture

        viewModel = CreateMissionViewModel(
            userRepository = userRepository,
            createMissionUseCase = createMissionUseCase,
            getUsersUseCase = getUsersUseCase,
            generateIdUseCase = generateIdUseCase
        )
    }

    @Test
    fun createViewModel_default_values_are_correct() {
        // Then
        assertEquals("", viewModel.uiState.value.title)
        assertEquals("", viewModel.uiState.value.description)
        assertEquals(LocalDate.now(), viewModel.uiState.value.startDate)
        assertEquals(LocalDate.now(), viewModel.uiState.value.endDate)
        assertEquals(SchoolLevel.all, viewModel.uiState.value.schoolLevels)
        assertEquals("", viewModel.uiState.value.duration)
        assertEquals(listOf(userFixture), viewModel.uiState.value.managers)
        assertEquals("", viewModel.uiState.value.maxParticipants)
        assertEquals(emptyList(), viewModel.uiState.value.missionTasks)
        assertEquals(usersFixture.missionManagerSorting(), viewModel.uiState.value.users)
        assertEquals("", viewModel.uiState.value.userQuery)
        assertEquals(SchoolLevel.all, viewModel.uiState.value.allSchoolLevels)
    }

    @Test
    fun onImageUriChange_should_update_image_uri() {
        // Given
        val imageUri = mockk<Uri>()

        // When
        viewModel.onImageUriChange(imageUri)

        // Then
        assertEquals(imageUri, viewModel.uiState.value.imageUri)
    }

    @Test
    fun onRemoveImageUri_should_remove_image_uri() {
        // When
        viewModel.onRemoveImageUri()

        // Then
        assertEquals(null, viewModel.uiState.value.imageUri)
    }

    @Test
    fun onTitleChange_should_update_title() {
        // Given
        val title = "title"

        // When
        viewModel.onTitleChange(title)

        // Then
        assertEquals(title, viewModel.uiState.value.title)
    }

    @Test
    fun onTitleChange_should_truncate_title_to_100_characters() {
        // Given
        val title = "a".repeat(100)
        val longTitle = "a".repeat(101)

        // When
        viewModel.onTitleChange(longTitle)

        // Then
        assertEquals(title, viewModel.uiState.value.title)
    }

    @Test
    fun onTitleChange_should_disable_create_when_title_is_empty() {
        // When
        viewModel.onTitleChange("")

        // Then
        assertEquals(false, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun onDescriptionChange_should_update_description() {
        // Given
        val description = "description"

        // When
        viewModel.onDescriptionChange(description)

        // Then
        assertEquals(description, viewModel.uiState.value.description)
    }

    @Test
    fun onDescriptionChange_should_truncate_description_to_1000_characters() {
        // Given
        val description = "a".repeat(1000)
        val longDescription = "a".repeat(1001)

        // When
        viewModel.onDescriptionChange(longDescription)

        // Then
        assertEquals(description, viewModel.uiState.value.description)
    }

    @Test
    fun onDescriptionChange_should_disable_create_when_description_is_empty() {
        // When
        viewModel.onDescriptionChange("")

        // Then
        assertEquals(false, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun onStartDateChange_should_update_start_date() {
        // Given
        val startDate = LocalDate.now().plusDays(4)

        // When
        viewModel.onStartDateChange(startDate)

        // Then
        assertEquals(startDate, viewModel.uiState.value.startDate)
    }

    @Test
    fun onStartDateChange_should_update_end_date_when_its_older() {
        // Given
        val date = LocalDate.now().plusDays(4)

        // When
        viewModel.onStartDateChange(date)

        // Then
        assertEquals(date, viewModel.uiState.value.endDate)
    }

    @Test
    fun onEndDateChange_should_update_end_date() {
        // Given
        val endDate = LocalDate.now().plusDays(4)

        // When
        viewModel.onEndDateChange(endDate)

        // Then
        assertEquals(endDate, viewModel.uiState.value.endDate)
    }

    @Test
    fun onEndDateChange_should_update_start_date_when_its_newer() {
        // Given
        val date = LocalDate.now().minusDays(4)

        // When
        viewModel.onEndDateChange(date)

        // Then
        assertEquals(date, viewModel.uiState.value.startDate)
    }

    @Test
    fun onSchoolLevelChange_should_add_school_level_when_not_present() {
        // Given
        val schoolLevelToAdd = SchoolLevel.GED_4
        val expectedResult = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3, SchoolLevel.GED_4)

        // When
        viewModel.onSchoolLevelChange(schoolLevelToAdd)
        viewModel.onSchoolLevelChange(schoolLevelToAdd)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.schoolLevels)
    }

    @Test
    fun onSchoolLevelChange_should_remove_school_level_when_present() {
        // Given
        val schoolLevelToRemove = SchoolLevel.GED_4
        val expectedResult = listOf(SchoolLevel.GED_1, SchoolLevel.GED_2, SchoolLevel.GED_3)


        // When
        viewModel.onSchoolLevelChange(schoolLevelToRemove)

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.schoolLevels)
    }

    @Test
    fun onSchoolLevelChange_should_update_school_levels_sorted() {
        // Given
        val schoolLevelsToRemove = listOf(SchoolLevel.GED_4, SchoolLevel.GED_1)
        val expectedResult = listOf(SchoolLevel.GED_2, SchoolLevel.GED_3)

        // When
        schoolLevelsToRemove.forEach {
            viewModel.onSchoolLevelChange(it)
        }

        // Then
        assertEquals(expectedResult, viewModel.uiState.value.schoolLevels)
    }

    @Test
    fun onMaxParticipantsChange_should_update_max_participants() {
        // Given
        val maxParticipants = "2"

        // When
        viewModel.onMaxParticipantsChange(maxParticipants)

        // Then
        assertEquals(maxParticipants, viewModel.uiState.value.maxParticipants)
    }

    @Test
    fun onMaxParticipantsChange_should_not_update_max_participants_when_empty() {
        // When
        viewModel.onMaxParticipantsChange(" ")

        // Then
        assertEquals("", viewModel.uiState.value.maxParticipants)
    }

    @Test
    fun onMaxParticipantsChange_should_not_update_max_participants_when_not_number() {
        // When
        viewModel.onMaxParticipantsChange("f")

        // Then
        assertEquals("", viewModel.uiState.value.maxParticipants)
    }

    @Test
    fun onMaxParticipantsChange_should_not_update_max_participants_when_inferior_to_0() {
        // When
        viewModel.onMaxParticipantsChange("00")

        // Then
        assertEquals("", viewModel.uiState.value.maxParticipants)
    }

    @Test
    fun onMaxParticipantsChange_should_disable_create_when_max_participants_is_empty() {
        // When
        viewModel.onMaxParticipantsChange("")

        // Then
        assertEquals(false, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun onMaxParticipantsChange_should_disable_create_when_max_participants_is_inferior_to_0() {
        // When
        viewModel.onMaxParticipantsChange("00")

        // Then
        assertEquals(false, viewModel.uiState.value.createEnabled)
    }

    @Test
    fun onDurationChange_should_update_duration() {
        // Given
        val duration = "duration"

        // When
        viewModel.onDurationChange(duration)

        // Then
        assertEquals(duration, viewModel.uiState.value.duration)
    }

    @Test
    fun onDurationChange_should_truncate_duration_to_200_characters() {
        // Given
        val duration = "a".repeat(200)
        val longDuration = "a".repeat(201)

        // When
        viewModel.onDurationChange(longDuration)

        // Then
        assertEquals(duration, viewModel.uiState.value.duration)
    }

    @Test
    fun onSaveManagers_should_update_managers() {
        // Given
        val managers = listOf(userFixture, userFixture2)

        // When
        viewModel.onSaveManagers(managers)

        // Then
        assertEquals(managers, viewModel.uiState.value.managers)
    }

    @Test
    fun onRemoveManager_should_remove_manager() {
        // Given
        val managers = listOf(userFixture, userFixture2)

        // When
        viewModel.onSaveManagers(managers)
        viewModel.onRemoveManager(userFixture2)

        // Then
        assertEquals(listOf(userFixture), viewModel.uiState.value.managers)
    }

    @Test
    fun onRemoveManager_should_not_remove_manager_when_its_last_user() {
        // Given
        val managers = listOf(userFixture)

        // When
        viewModel.onSaveManagers(managers)
        viewModel.onRemoveManager(userFixture)

        // Then
        assertEquals(managers, viewModel.uiState.value.managers)
    }

    @Test
    fun onUserQueryChange_should_update_userQuery() {
        // Given
        val userQuery = "query"

        // When
        viewModel.onUserQueryChange(userQuery)

        // Then
        assertEquals(userQuery, viewModel.uiState.value.userQuery)
    }

    @Test
    fun onUserQueryChange_should_filter_users_by_name() {
        // Given
        val user = userFixture.copy(firstName = "ab")
        val userQuery = "a"
        val users = listOf(user, userFixture2.copy(firstName = "b", lastName = "b"))
        coEvery { getUsersUseCase() } returns users

        // When
        viewModel = CreateMissionViewModel(
            userRepository = userRepository,
            createMissionUseCase = createMissionUseCase,
            getUsersUseCase = getUsersUseCase,
            generateIdUseCase = generateIdUseCase
        )
        viewModel.onUserQueryChange(userQuery)

        // Then
        assertEquals(listOf(user), viewModel.uiState.value.users)
    }

    @Test
    fun onUserQueryChange_should_reset_users_to_default_when_is_blank() {
        // When
        viewModel.onUserQueryChange("")

        // Then
        assertEquals(usersFixture.missionManagerSorting(), viewModel.uiState.value.users)
    }

    @Test
    fun onResetQuery_should_reset_query() {
        // When
        viewModel.onResetUserQuery()

        // Then
        assertEquals("", viewModel.uiState.value.userQuery)
    }

    @Test
    fun onResetQuery_should_reset_users_to_default() {
        // When
        viewModel.onResetUserQuery()

        // Then
        assertEquals(usersFixture.missionManagerSorting(), viewModel.uiState.value.users)
    }

    @Test
    fun onAddTask_should_add_Mission_task() {
        // Given
        val task = MissionTask(newId, "task")

        // When
        viewModel.onAddMissionTask(task.value)

        // Then
        assertEquals(listOf(task), viewModel.uiState.value.missionTasks)
    }

    @Test
    fun onAddTask_should_trim_Mission_task() {
        // Given
        val task = MissionTask(newId, " task ")
        val trimmedTask = task.copy(value = "task")

        // When
        viewModel.onAddMissionTask(task.value)

        // Then
        assertEquals(listOf(trimmedTask), viewModel.uiState.value.missionTasks)
    }

    @Test
    fun onEditTask_should_edit_Mission_task() {
        // Given
        val task = MissionTask(newId, "task")
        val editedTask = task.copy(value = "editedTask")

        // When
        viewModel.onAddMissionTask(task.value)
        viewModel.onEditMissionTask(editedTask)

        // Then
        assertEquals(listOf(editedTask), viewModel.uiState.value.missionTasks)
    }

    @Test
    fun onEditTask_should_trimmed_Mission_task() {
        // Given
        val task = MissionTask(newId, "task")
        val editedTask = task.copy(value = " editedTask ")
        val trimmedEditedTask = task.copy(value = "editedTask")

        // When
        viewModel.onAddMissionTask(task.value)
        viewModel.onEditMissionTask(editedTask)

        // Then
        assertEquals(listOf(trimmedEditedTask), viewModel.uiState.value.missionTasks)
    }

    @Test
    fun onRemoveTask_should_remove_Mission_task() {
        // Given
        val task = MissionTask(newId, "task")

        // When
        viewModel.onAddMissionTask(task.value)
        viewModel.onRemoveMissionTask(task)

        // Then
        assertEquals(emptyList(), viewModel.uiState.value.missionTasks.map { it.value })
    }
}