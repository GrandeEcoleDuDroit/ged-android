package com.upsaclay.news

import com.upsaclay.common.domain.ConnectivityObserver
import com.upsaclay.news.domain.longAnnouncementFixture
import com.upsaclay.news.domain.repository.AnnouncementRepository
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementViewModel
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class EditAnnouncementViewModelTest {
    private val announcementRepository: AnnouncementRepository = mockk()
    private val connectivityObserver: ConnectivityObserver = mockk()

    private lateinit var viewModel: EditAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "Title"
    private val content = "Content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        every { connectivityObserver.isConnected } returns true

        viewModel = EditAnnouncementViewModel(
            announcement = longAnnouncementFixture,
            announcementRepository = announcementRepository,
            connectivityObserver = connectivityObserver
        )
    }

    @Test
    fun updateTitle_should_on_titleChange() {
        // When
        viewModel.onTitleChange(title)

        // Then
        assertEquals(title, viewModel.uiState.value.title)
    }

    @Test
    fun updateContent_should_on_contentChange() {
        // When
        viewModel.onContentChange(content)

        // Then
        assertEquals(content, viewModel.uiState.value.content)
    }

    @Test
    fun updateAnnouncement_should_updateAnnouncement() {
        // Given
        viewModel.onTitleChange(title)
        viewModel.onContentChange(content)

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify { announcementRepository.updateAnnouncement(any()) }
    }

    @Test
    fun updateAnnouncement_should_not_update_when_content_is_empty() {
        // Given
        viewModel.onTitleChange("title")
        viewModel.onContentChange("")

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify(exactly = 0) { announcementRepository.updateAnnouncement(any()) }
    }

    @Test
    fun updateAnnouncement_should_not_update_when_title_and_content_are_same() {
        // Given
        viewModel.onTitleChange(longAnnouncementFixture.title!!)
        viewModel.onContentChange(longAnnouncementFixture.content)

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify(exactly = 0) { announcementRepository.updateAnnouncement(any()) }
    }

    @Test
    fun announcement_should_be_trim_when_updated() {
        // Given
        val titleWithSpaces = "  ${longAnnouncementFixture.title}  "
        val contentWithSpaces = "  ${longAnnouncementFixture.content}  "
        viewModel.onTitleChange(titleWithSpaces)
        viewModel.onContentChange(contentWithSpaces)

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify {
            announcementRepository.updateAnnouncement(
                longAnnouncementFixture.copy(
                    title = titleWithSpaces.trim(),
                    content = contentWithSpaces.trim()
                )
            )
        }
    }
}