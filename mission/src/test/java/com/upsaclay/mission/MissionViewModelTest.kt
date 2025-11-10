package com.upsaclay.mission

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.common.domain.repository.UserRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.missionsFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import com.upsaclay.mission.domain.usecase.RefreshMissionsUseCase
import com.upsaclay.mission.domain.usecase.ResendMissionUseCase
import com.upsaclay.mission.presentation.MissionViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MissionViewModelTest {
    private val missionRepository: MissionRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val resendMissionUseCase: ResendMissionUseCase = mockk()
    private val deleteMissionUseCase: DeleteMissionUseCase = mockk()
    private val refreshMissionsUseCase: RefreshMissionsUseCase = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var viewModel: MissionViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { connectivityObserver.isConnected } returns true
        every { missionRepository.missions } returns flowOf(missionsFixture)
        every { userRepository.user } returns MutableStateFlow(userFixture)
        every { resendMissionUseCase(any()) } returns Unit
        coEvery { deleteMissionUseCase(any()) } returns Unit
        coEvery { refreshMissionsUseCase() } returns Unit

        viewModel = MissionViewModel(
            missionRepository = missionRepository,
            userRepository = userRepository,
            resendMissionUseCase = resendMissionUseCase,
            deleteMissionUseCase = deleteMissionUseCase,
            refreshMissionsUseCase = refreshMissionsUseCase,
            connectivityObserver = connectivityObserver
        )
    }

    @Test
    fun refreshMissions_should_refresh_missions() = runTest {
        // When
        viewModel.refreshMissions()

        // Then
        coVerify { refreshMissionsUseCase() }
    }

    @Test
    fun resendMission_should_resend_mission() = runTest {
        // When
        viewModel.resendMission(missionFixture)

        // Then
        coVerify { resendMissionUseCase(missionFixture) }
    }

    @Test
    fun deleteMission_should_delete_mission() = runTest {
        // When
        viewModel.deleteMission(missionFixture)

        // Then
        coVerify { deleteMissionUseCase(missionFixture) }
    }
}