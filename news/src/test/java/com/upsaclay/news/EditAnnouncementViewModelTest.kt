package com.upsaclay.news

import com.upsaclay.news.domain.announcement.announcementFixture
import com.upsaclay.news.domain.announcement.AnnouncementRepository
import com.upsaclay.news.presentation.announcement.editannouncement.EditAnnouncementViewModel
import io.mockk.coVerify
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

    private lateinit var viewModel: EditAnnouncementViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    private val title = "Title"
    private val content = "Content"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = EditAnnouncementViewModel(
            announcement = announcementFixture,
            announcementRepository = announcementRepository
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
        viewModel.onTitleChange(announcementFixture.title!!)
        viewModel.onContentChange(announcementFixture.content)

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify(exactly = 0) { announcementRepository.updateAnnouncement(any()) }
    }

    @Test
    fun announcement_should_be_trim_when_updated() {
        // Given
        val titleWithSpaces = "  ${announcementFixture.title}  "
        val contentWithSpaces = "  ${announcementFixture.content}  "
        viewModel.onTitleChange(titleWithSpaces)
        viewModel.onContentChange(contentWithSpaces)

        // When
        viewModel.updateAnnouncement()

        // Then
        coVerify {
            announcementRepository.updateAnnouncement(
                announcementFixture.copy(
                    title = titleWithSpaces.trim(),
                    content = contentWithSpaces.trim()
                )
            )
        }
    }
}