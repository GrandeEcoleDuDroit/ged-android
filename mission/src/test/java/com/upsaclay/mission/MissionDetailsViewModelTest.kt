package com.upsaclay.mission

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.mission.domain.entity.MissionReport
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
        every { userRepository.user } returns flowOf(userFixture2)
        coEvery { missionRepository.removeParticipant(any(), any()) } returns Unit
        coEvery { missionRepository.reportMission(any()) } returns Unit
        coEvery { missionRepository.addParticipant(any()) } returns Unit
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
        assertEquals(userFixture2, viewModel.uiState.value.user)
        assertEquals(missionFixture, viewModel.uiState.value.mission)
        assertEquals(false, viewModel.uiState.value.loading)
    }

    @Test
    fun unregisterFromMission_should_remove_current_user_from_mission() {
        // When
        viewModel.unregisterFromMission()

        // Then
        coVerify { missionRepository.removeParticipant(missionFixture.id, userFixture2.id) }
    }

    @Test
    fun reportMission_should_report_mission() {
        // Given
        val report = MissionReport(
            missionId = missionFixture.id,
            userInfo = MissionReport.UserInfo(
                fullName = userFixture2.fullName,
                email = userFixture2.email
            ),
            reason = MissionReport.Reason.FALSE_INFORMATION
        )

        // When
        viewModel.reportMission(report)

        // Then
        coVerify { missionRepository.reportMission(report) }
    }

    @Test
    fun deleteMission_should_delete_mission() {
        // When
        viewModel.deleteMission()

        // Then
        coVerify { deleteMissionUseCase(missionFixture) }
    }

    @Test
    fun remoteParticipant_should_remove_participant() {
        // When
        viewModel.removeParticipant(userFixture2.id)

        // Then
        coVerify { missionRepository.removeParticipant(missionFixture.id, userFixture2.id) }
    }

    @Test
    fun button_state_should_be_register_enabled_when_mission_is_not_full_and_user_is_not_registered() {
        // Given
        val mission = missionFixture.copy(
            participants = emptyList()
        )
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // When
        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            deleteMissionUseCase = deleteMissionUseCase,
            connectivityObserver = connectivityObserver
        )

        // Then
        assertEquals(MissionDetailsViewModel.MissionButtonState.Register(), viewModel.uiState.value.buttonState)
    }

    @Test
    fun button_state_should_be_register_disabled_when_mission_is_full_and_user_is_not_registered() {
        // Given
        val user = userFixture2.copy(id = "3")
        val mission = missionFixture.copy(
            maxParticipants = 1,
            participants = listOf(user)
        )
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // When
        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            deleteMissionUseCase = deleteMissionUseCase,
            connectivityObserver = connectivityObserver
        )

        // Then
        assertEquals(MissionDetailsViewModel.MissionButtonState.Register(false), viewModel.uiState.value.buttonState)
    }

    @Test
    fun button_state_should_be_registered_when_user_is_registered() {
        // Given
        val mission = missionFixture.copy(
            maxParticipants = 1,
            participants = listOf(userFixture2)
        )
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // When
        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            deleteMissionUseCase = deleteMissionUseCase,
            connectivityObserver = connectivityObserver
        )

        // Then
        assertEquals(MissionDetailsViewModel.MissionButtonState.Registered, viewModel.uiState.value.buttonState)
    }

    @Test
    fun button_state_should_be_complete_when_mission_is_expired() {
        // Given
        val mission = missionFixture.copy(endDate = LocalDate.now().minusDays(1))
        every { missionRepository.getMissionFlow(any()) } returns flowOf(mission)

        // When
        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            deleteMissionUseCase = deleteMissionUseCase,
            connectivityObserver = connectivityObserver
        )

        // Then
        assertEquals(MissionDetailsViewModel.MissionButtonState.Complete, viewModel.uiState.value.buttonState )
    }

    @Test
    fun button_state_should_be_hidden_when_user_is_manager() {
        // Given
        every { userRepository.user } returns flowOf(userFixture)

        // When
        viewModel = MissionDetailsViewModel(
            missionId = missionFixture.id,
            missionRepository = missionRepository,
            userRepository = userRepository,
            deleteMissionUseCase = deleteMissionUseCase,
            connectivityObserver = connectivityObserver
        )

        // Then
        assertEquals(MissionDetailsViewModel.MissionButtonState.Hidden, viewModel.uiState.value.buttonState )
    }
}