import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.mission.domain.entity.Mission.MissionState
import com.upsaclay.mission.domain.missionFixture
import com.upsaclay.mission.domain.repository.MissionRepository
import com.upsaclay.mission.domain.usecase.UpdateMissionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.File

class UpdateMissionUseCaseTest {
    private val missionRepository: MissionRepository = mockk()
    private val imageRepository: ImageRepository = mockk()

    private lateinit var useCase: UpdateMissionUseCase
    private val imageUri = "imageUri"
    private val imageUrl = "imageUrl"
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { imageRepository.createCacheImage(any(), any()) } returns file
        coEvery { missionRepository.updateMission(any(), any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any(), any()) } returns Unit
        coEvery { imageRepository.deleteRemoteImage(any()) } returns Unit

        useCase = UpdateMissionUseCase(
            missionRepository = missionRepository,
            imageRepository = imageRepository
        )
    }

    @Test
    fun updateMissionUseCase_should_create_cache_image_when_image_uri_is_not_null() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Published())

        // When
        useCase(mission, imageUri, MissionState.Published())

        // Then
        coVerify {
            imageRepository.createCacheImage(any(), imageUri)
        }
        coVerify {
            missionRepository.updateMission(
                mission.copy(state = MissionState.Published(file.name)), file
            )
        }
    }

    @Test
    fun updateMissionUseCase_should_delete_old_mission_image_when_its_present() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Published())

        // When
        useCase(
            mission = mission,
            newImageUri = imageUri,
            oldMissionState = MissionState.Published(imageUrl))

        // Then
        coVerify { imageRepository.deleteRemoteImage(imageUrl) }
    }

    @Test
    fun updateMissionUseCase_should_delete_local_mission_image_when_was_created() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Published(imageUrl))

        // When
        useCase(mission, imageUri, MissionState.Published())

        // Then
        coVerify { imageRepository.deleteLocalImage(any(), file.name) }
    }
}