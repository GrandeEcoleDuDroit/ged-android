
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.UpsertMissionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpsertMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpsertMissionUseCase

    @Before
    fun setUp() {
        coEvery { missionRepository.getLocalMission(any()) } returns missionFixture
        coEvery { missionRepository.upsertLocalMission(any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit
        coEvery { imageRepository.deleteCacheImage(any()) } returns Unit

        useCase = UpsertMissionUseCase(
            missionRepository = missionRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun upsertMissionUseCase_should_upsert_mission() = runTest {
        // When
        useCase.execute(missionFixture)

        // Then
        coVerify { missionRepository.upsertLocalMission(missionFixture) }
    }

    @Test
    fun upsertMissionUseCase_should_delete_local_image_when_present() = runTest {
        // Given
        val imagePath = "path"
        val mission = missionFixture.copy(state = Mission.MissionState.Publishing(imagePath))
        coEvery { missionRepository.getLocalMission(any()) } returns mission

        // When
        useCase.execute(missionFixture)

        // Then
        coVerify { imageRepository.deleteLocalImage(imagePath) }
    }
}