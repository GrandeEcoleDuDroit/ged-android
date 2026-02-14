
import com.upsaclay.common.domain.entity.SchoolLevel
import com.upsaclay.common.domain.repository.ImageRepository
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.domain.userFixture2
import com.upsaclay.common.domain.userFixture3
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
    private val file = File("file")

    @Before
    fun setUp() {
        coEvery { imageRepository.getFileExtension(any()) } returns ""
        coEvery { imageRepository.createCacheImage(any(), any()) } returns file
        coEvery { missionRepository.updateMission(any(), any(), any()) } returns Unit
        coEvery { imageRepository.deleteLocalImage(any()) } returns Unit
        coEvery { imageRepository.deleteCacheImage(any()) } returns Unit

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
        useCase.execute(userFixture, mission, imageUri)

        // Then
        coVerify {
            imageRepository.createCacheImage(any(), imageUri)
        }
    }

    @Test
    fun updateMissionUseCase_should_delete_created_cache_images() = runTest {
        // Given
        val mission = missionFixture.copy(state = MissionState.Published())

        // When
        useCase.execute(userFixture, mission, imageUri)

        // Then
        coVerify { imageRepository.deleteCacheImage(any()) }
    }

    @Test
    fun updateMissionUseCase_should_remove_participants_with_unmatched_school_level() = runTest {
        // Given
        val participants = listOf(
            userFixture2.copy(schoolLevel = SchoolLevel.GED_1),
            userFixture3.copy(schoolLevel = SchoolLevel.GED_2)
        )
        val mission = missionFixture.copy(
            schoolLevels = listOf(SchoolLevel.GED_2),
            participants = participants
        )
        val expectedMission = mission.copy(participants = listOf(participants[1]))

        // When
        useCase.execute(userFixture, mission, null)

        // Then
        coVerify { missionRepository.updateMission(any(), expectedMission, any()) }
    }
}