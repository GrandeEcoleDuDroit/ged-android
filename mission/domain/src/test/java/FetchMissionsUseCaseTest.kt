
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.missionsFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.mission.domain.usecase.UpsertLocalMissionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class FetchMissionsUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val upsertLocalMissionUseCase: UpsertLocalMissionUseCase = mockk()

    private lateinit var useCase: FetchMissionsUseCase

    @Before
    fun setUp() {
        every { missionRepository.missions } returns flowOf(missionsFixture)
        every { missionRepository.currentMissions } returns missionsFixture
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit
        coEvery { missionRepository.deleteLocalMission(any()) } returns Unit
        coEvery { missionRepository.getRemoteMissions() } returns missionsFixture
        coEvery { missionRepository.deleteLocalMission(any()) } returns Unit
        coEvery { upsertLocalMissionUseCase.execute(any()) } returns Unit

        useCase = FetchMissionsUseCase(
            missionRepository = missionRepository,
            upsertLocalMissionUseCase = upsertLocalMissionUseCase
        )
    }

    @Test
    fun fetchMissions_should_upsert_new_remote_mission() = runTest {
        // Given
        every { missionRepository.currentMissions } returns emptyList()
        coEvery { missionRepository.getRemoteMissions() } returns listOf(missionFixture)

        // When
        useCase.execute()

        // Then
        coVerify { upsertLocalMissionUseCase.execute(missionFixture) }
    }

    @Test
    fun fetchMissions_should_delete_missions_non_present_in_remote() = runTest {
        // Given
        every { missionRepository.currentMissions } returns listOf(missionFixture)
        coEvery { missionRepository.getRemoteMissions() } returns emptyList()

        // When
        useCase.execute()

        // Then
        coVerify { missionRepository.deleteLocalMission(missionFixture) }
    }
}