package com.upsaclay.news

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.upsaclay.common.domain.userFixture
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.presentation.allAnnouncement.AllAnnouncementDestination
import com.upsaclay.news.presentation.allAnnouncement.AllAnnouncementViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AllAnnouncementScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    private val allAnnouncementViewModel: AllAnnouncementViewModel = mockk()
    private val uiState = AllAnnouncementViewModel.AllAnnouncementUiState(
        user = userFixture,
        announcements = announcementsFixture,
        refreshing = false
    )

    @Before
    fun setUp() {
        every { allAnnouncementViewModel.event } returns MutableSharedFlow()
        every { allAnnouncementViewModel.uiState } returns MutableStateFlow(uiState)
        coEvery { allAnnouncementViewModel.refreshAnnouncements() } returns Unit
    }

    @Test
    fun empty_announcements_show_empty_announcement_text() {
        // Given
        every { allAnnouncementViewModel.uiState } returns MutableStateFlow(uiState.copy(announcements = emptyList()))

        // When
        rule.setContent {
            AllAnnouncementDestination(
                onAnnouncementClick = {},
                onBackClick = {},
                viewModel = allAnnouncementViewModel,
                bottomBar = {}
            )
        }

        // Then
        rule.onNodeWithTag(rule.activity.getString(R.string.news_screen_empty_announcement_text_tag))
            .assertExists()
    }
}