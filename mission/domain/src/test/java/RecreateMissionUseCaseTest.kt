import com.upsaclay.common.domain.repository.FileRepository
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.RecreateMissionUseCase
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
class RecreateMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val fileRepository: FileRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: RecreateMissionUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { missionRepository.createMission(any(), any()) } returns Unit
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit
        coEvery { fileRepository.getFile(any()) } returns file
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit

        useCase = RecreateMissionUseCase(
            missionRepository = missionRepository,
            fileRepository = fileRepository,
            imageRepository = imageRepository,
            scope = testScope
        )
    }

    @Test
    fun resendMissionUseCase_should_resend_mission_when_state_is_error_only() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.createMission(any(), null)
        }
    }

    @Test
    fun resendMissionUseCase_should_create_mission_with_publishing_state() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.createMission(mission.copy(state = MissionState.Publishing()), null)
        }
    }

    @Test
    fun resendMissionUseCase_should_create_mission_with_publishing_state_and_image_path_when_image_uri_is_provided() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.createMission(mission.copy(state = MissionState.Publishing(file.path)), file)
        }
    }

    @Test
    fun resendMissionUseCase_should_update_local_mission_to_published_state_when_succeeds() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published()))
        }
    }

    @Test
    fun resendMissionUseCase_should_update_local_mission_to_published_state_with_image_name_when_succeeds_and_image_uri_is_provided() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published(file.name)))
        }
    }

    @Test
    fun resendMissionUseCase_should_update_local_mission_to_error_state_when_fails() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())
        coEvery { missionRepository.createMission(any(), any()) } throws Exception()

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error()))
        }
    }

    @Test
    fun resendMissionUseCase_should_update_local_mission_to_error_state_and_image_path_when_image_uri_is_provided_when_fails() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))
        coEvery { missionRepository.createMission(any(), any()) } throws Exception()

        // When
        useCase(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error(file.path)))
        }
    }

    @Test
    fun resendMissionUseCase_should_delete_local_image_when_succeed() {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase(mission)

        // Then
        coVerify {
            imageRepository.deleteLocalImage(file.path)
        }
    }
}