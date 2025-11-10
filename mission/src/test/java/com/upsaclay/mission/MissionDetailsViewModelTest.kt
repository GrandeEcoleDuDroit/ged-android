package com.upsaclay.mission

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import com.upsaclay.mission.presentation.missiondetails.MissionDetailsViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

class MissionDetailsViewModelTest {
    private val missionRepository: MissionRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()
    private val deleteMissionUseCase: DeleteMissionUseCase = mockk()

    private lateinit var viewModel: MissionDetailsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { connectivityObserver.isConnected } returns true
        every { missionRepository.getMissionFlow(any()) } returns flowOf(missionFixture)
        every { userRepository.user } returns flowOf(userFixture)
        coEvery { deleteMissionUseCase(any()) } returns Unit

        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            connectivityObserver = connectivityObserver,
            deleteMissionUseCase = deleteMissionUseCase
        )
    }

    @Test
    fun missionDetailsViewModel_default_values_are_correct() {
        // Then
        assertEquals(userFixture, viewModel.uiState.value.user)
        assertEquals(missionFixture, viewModel.uiState.value.mission)
        assertEquals(false, viewModel.uiState.value.loading)
        assertEquals(true, viewModel.uiState.value.registrationDisabled)
    }

    @Test
    fun deleteMission_should_delete_mission() {
        // When
        viewModel.deleteMission()

        // Then
        coVerify { deleteMissionUseCase(missionFixture) }
    }

    @Test
    fun registration_should_be_disabled_when_user_school_level_not_match() {
        // Given
        val mission = missionFixture.copy(schoolLevels = listOf(SchoolLevel.GED_1))
        val user = userFixture.copy(schoolLevel = SchoolLevel.GED_2)
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)
        every { userRepository.user } returns flowOf(user)

        // Then
        assert(viewModel.uiState.value.registrationDisabled == true)
    }

    @Test
    fun registration_should_be_disabled_when_mission_is_expired() {
        // Given
        val mission = missionFixture.copy(endDate = LocalDate.now().minusDays(1))
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // Then
        assert(viewModel.uiState.value.registrationDisabled == true)
    }

    @Test
    fun registration_should_be_disabled_when_mission_is_full() {
        // Given
        val mission = missionFixture.copy(maxParticipants = 1, participants = listOf(userFixture))
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // Then
        assert(viewModel.uiState.value.registrationDisabled == true)
    }
}