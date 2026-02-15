import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.MissionJobQueue
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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class RecreateMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val imageRepository: ImageRepository = mockk()
    private val missionJobQueue: MissionJobQueue = mockk()

    private lateinit var useCase: RecreateMissionUseCase
    private val testScope = TestScope(UnconfinedTestDispatcher())
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { missionJobQueue.addJob(any(), any()) } returns Unit
        coEvery { missionJobQueue.cancelAndRemoveJob(any()) } returns Unit
        coEvery { missionRepository.createMission(any(), any()) } returns Unit
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit

        useCase = RecreateMissionUseCase(
            missionRepository = missionRepository,
            imageRepository = imageRepository,
            missionJobQueue = missionJobQueue,
            scope = testScope
        )
    }

    @Test
    fun recreateMissionUseCase_should_recreate_mission_when_state_is_error_only() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.createMission(any(), null)
        }
    }

    @Test
    fun recreateMissionUseCase_should_create_mission_with_publishing_state() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.createMission(mission.copy(state = MissionState.Publishing()), null)
        }
    }

    @Test
    fun recreateMissionUseCase_should_create_mission_with_publishing_state_and_image_path_when_image_uri_is_provided() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.createMission(mission.copy(state = MissionState.Publishing(file.path)), any())
        }
    }

    @Test
    fun recreateMissionUseCase_should_update_local_mission_to_published_state_when_succeeds() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published()))
        }
    }

    @Test
    fun recreateMissionUseCase_should_update_local_mission_to_published_state_with_image_name_when_succeeds_and_image_uri_is_provided() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Published(file.name)))
        }
    }

    @Test
    fun recreateMissionUseCase_should_update_local_mission_to_error_state_when_fails() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error())
        coEvery { missionRepository.createMission(any(), any()) } throws Exception()

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error()))
        }
    }

    @Test
    fun recreateMissionUseCase_should_update_local_mission_to_error_state_with_image_path_when_fails_and_image_uri_is_provided() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))
        coEvery { missionRepository.createMission(any(), any()) } throws Exception()

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            missionRepository.upsertLocalMission(mission.copy(state = MissionState.Error(file.path)))
        }
    }

    @Test
    fun recreateMissionUseCase_should_delete_local_image_when_succeed() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Error(file.path))

        // When
        useCase.execute(mission)

        // Then
        coVerify {
            imageRepository.deleteLocalImage(file.path)
        }
    }

    @Test
    fun recreateMission_should_store_job_reference() = runTest {
        // When
        useCase.execute(missionFixture.copy(state = MissionState.Error()))

        // Then
        coVerify {
            missionJobQueue.addJob(any(), missionFixture.id)
        }
    }

    @Test
    fun recreateMission_should_remove_job_reference_when_job_finished() = runTest {
        // When
        useCase.execute(missionFixture.copy(state = MissionState.Error()))

        // Then
        coVerify { missionJobQueue.cancelAndRemoveJob(missionFixture.id) }
    }
}