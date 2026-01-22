import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.missionsFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
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

    private lateinit var useCase: FetchMissionsUseCase

    @Before
    fun setUp() {
        every { missionRepository.missions } returns flowOf(missionsFixture)
        every { missionRepository.currentMissions } returns missionsFixture
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit
        coEvery { missionRepository.deleteLocalMission(any()) } returns Unit
        coEvery { missionRepository.getRemoteMissions() } returns missionsFixture

        useCase = FetchMissionsUseCase(
            missionRepository = missionRepository
        )
    }

    @Test
    fun synchronizeMissions_should_upsert_new_remote_mission() = runTest {
        // Given
        every { missionRepository.currentMissions } returns emptyList()
        coEvery { missionRepository.getRemoteMissions() } returns listOf(missionFixture)

        // When
        useCase.execute()

        // Then
        coVerify { missionRepository.upsertLocalMission(missionFixture) }
    }

    @Test
    fun synchronizeMissions_should_delete_missions_non_present_in_remote() = runTest {
        // Given
        every { missionRepository.currentMissions } returns listOf(missionFixture)
        coEvery { missionRepository.getRemoteMissions() } returns emptyList()

        // When
        useCase.execute()

        // Then
        coVerify { missionRepository.deleteLocalMission(missionFixture) }
    }
}