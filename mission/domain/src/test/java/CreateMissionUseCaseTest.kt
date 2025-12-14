import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.CreateMissionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class CreateMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: CreateMissionUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val imageUri = "imageUri"
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { imageRepository.getFileExtension(any()) } returns ""
        coEvery { imageRepository.createLocalImage(any(), any()) } returns file
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit
        coEvery { missionRepository.createMission(any(), any()) } returns Unit
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit

        useCase = CreateMissionUseCase(
            missionRepository = missionRepository,
            imageRepository = imageRepository,
            scope = testScope
        )
    }

    @Test
    fun createMissionUseCase_should_create_local_image_when_image_uri_is_provided() {
        // When
        useCase(missionFixture, imageUri)

        // Then
        coVerify {
            imageRepository.createLocalImage(any(), imageUri)
        }
    }

    @Test
    fun createMission_should_create_mission_with_publishing_state() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Draft)

        // When
        useCase(mission, null)

        // Then
        coVerify {
            missionRepository.createMission(mission.copy(state = MissionState.Publishing()), null)
        }
    }

    @Test
    fun createMission_should_update_local_mission_to_published_state_when_succeeds() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Draft)

        // When
        useCase(mission, null)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published()))
        }
    }

    @Test
    fun createMission_should_update_local_mission_to_error_state_when_fails() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Draft)
        coEvery { missionRepository.createMission(any(), any()) } throws Exception()

        // When
        useCase(mission, null)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error()))
        }
    }

    @Test
    fun createMission_should_delete_local_image_when_succeed() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Draft)

        // When
        useCase(mission, imageUri)

        // Then
        coVerify {
            imageRepository.deleteLocalImage(any())
        }
    }
}