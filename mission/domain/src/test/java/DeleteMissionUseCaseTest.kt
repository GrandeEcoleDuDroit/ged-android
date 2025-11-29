import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.DeleteMissionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: DeleteMissionUseCase
    private val imageUrl = "imageUrl"
    private val imagePath = "imagePath"

    @Before
    fun setUp() {
        coEvery { missionRepository.deleteMission(any(), any()) } returns Unit
        coEvery { missionRepository.deleteLocalMission(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any(), any()) } returns Unit

        useCase = DeleteMissionUseCase(
            missionRepository = missionRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun deleteMissionUseCase_should_delete_mission_when_state_is_published() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Published(imageUrl))

        // When
        useCase(mission = mission)

        // Then
        coVerify { missionRepository.deleteMission(mission, imageUrl) }
    }

    @Test
    fun deleteMissionUseCase_should_delete_local_mission_when_state_is_not_published() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Draft)

        // When
        useCase(mission = mission)

        // Then
        coEvery { missionRepository.deleteLocalMission(mission) }
    }

    @Test
    fun deleteMissionUseCase_should_delete_local_image_when_state_is_not_published_and_image_path_is_not_null() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(imagePath))

        // When
        useCase(mission = mission)

        // Then
        coVerify { imageRepository.deleteLocalImage(any(), imagePath) }
    }
}