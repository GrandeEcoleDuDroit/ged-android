import com.upsaclay.mission.domain.usecase.FetchMissionsUseCase
import com.upsaclay.mission.domain.usecase.RefreshMissionsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RefreshMissionsUseCaseTest {
    private val fetchMissionsUseCase: FetchMissionsUseCase = mockk()

    private lateinit var useCase: RefreshMissionsUseCase

    @Before
    fun setUp() {
        coEvery { fetchMissionsUseCase() } returns Unit

        useCase = RefreshMissionsUseCase(
            fetchMissionsUseCase = fetchMissionsUseCase
        )
    }

    @Test
    fun refreshMissionsUseCase_should_synchronize_announcements_when_debounce_interval_exceeded() = runTest {
        // When
        useCase()

        // Then
        assert(useCase.lastRequestTime > 0)
        coVerify { fetchMissionsUseCase() }
    }

    @Test
    fun refreshAnnouncement_should_not_refresh_when_debounce_interval_not_exceeded() = runTest {
        // Given
        val currentTime = System.currentTimeMillis()
        useCase.lastRequestTime = currentTime

        // When
        useCase()

        // Then
        assert(useCase.lastRequestTime == currentTime)
        coVerify(exactly = 0) { fetchMissionsUseCase() }
    }
}