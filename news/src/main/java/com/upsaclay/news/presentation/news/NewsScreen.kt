package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.LoadingScreen
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcement.Announcement
import com.upsaclay.news.domain.announcement.AnnouncementReport
import com.upsaclay.news.domain.announcement.announcementsFixture
import com.upsaclay.news.presentation.announcement.components.AnnouncementBottomSheet
import com.upsaclay.news.presentation.announcement.stringRes
import com.upsaclay.news.presentation.news.components.NewsScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDestination(
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementsClick: () -> Unit,
    onCreatePostClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: NewsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackBar = { message: String ->
        scope.launch {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> showSnackBar(context.getString(event.messageId))
                is SingleUiEvent.Success -> showSnackBar(context.getString(event.messageId))
            }
        }
    }

    if (uiState.user != null) {
        NewsScreen(
            user = uiState.user!!,
            announcements = uiState.announcements,
            refreshing = uiState.refreshing,
            loading = uiState.loading,
            bottomBar = bottomBar,
            snackbarHostState = snackbarHostState,
            onRefresh = viewModel::refreshAnnouncements,
            onAnnouncementClick = onAnnouncementClick,
            onCreateAnnouncementClick = onCreateAnnouncementClick,
            onResendAnnouncementClick = viewModel::resendAnnouncement,
            onEditAnnouncementClick = { viewModel.getAnnouncement(it)?.let(onEditAnnouncementClick) },
            onDeleteAnnouncementClick = viewModel::deleteAnnouncement,
            onSeeAllAnnouncementsClick = onSeeAllAnnouncementsClick,
            onReportAnnouncementClick = viewModel::reportAnnouncement,
            onCreatePostClick = onCreatePostClick
        )
    } else {
        LoadingScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsScreen(
    user: User,
    announcements: List<Announcement>?,
    refreshing: Boolean,
    loading: Boolean,
    bottomBar: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onRefresh: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onResendAnnouncementClick: (Announcement) -> Unit,
    onEditAnnouncementClick: (String) -> Unit,
    onDeleteAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementsClick: () -> Unit,
    onReportAnnouncementClick: (AnnouncementReport) -> Unit,
    onCreatePostClick: () -> Unit
) {
    var activeBottomSheet by remember { mutableStateOf<NewsScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<NewsDialog?>(null) }

    when(val dialogType = activeDialog) {
        is NewsDialog.DeleteAnnouncementDialog -> {
            DefaultDialog(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
                text = stringResource(id = R.string.delete_announcement_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteAnnouncementClick(dialogType.announcement)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
    }

    if (loading) {
        LoadingDialog()
    }

    NewsScaffold(
        user = user,
        onCreatePostClick = onCreatePostClick,
        onCreateAnnouncementClick = onCreateAnnouncementClick,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    ) { paddingValues ->
        PullToRefreshComponent(
            modifier = Modifier.padding(paddingValues),
            onRefresh = onRefresh,
            refreshing = refreshing
        ) {
            RecentAnnouncementSection(
                announcements = announcements,
                onAnnouncementClick = onAnnouncementClick,
                onUncreatedAnnouncementClick = { announcement ->
                    activeBottomSheet = NewsScreenBottomSheet.AnnouncementBottomSheet(announcement)
                },
                onSeeAllAnnouncementsClick = onSeeAllAnnouncementsClick,
                onAnnouncementOptionClick = { announcement ->
                    activeBottomSheet = NewsScreenBottomSheet.AnnouncementBottomSheet(announcement)
                }
            )
        }

        when(val bottomSheet = activeBottomSheet) {
            is NewsScreenBottomSheet.AnnouncementBottomSheet -> {
                AnnouncementBottomSheet(
                    announcementState = bottomSheet.announcement.state,
                    isEditable = user.admin && bottomSheet.announcement.author.id == user.id,
                    onEditClick = {
                        activeBottomSheet = null
                        onEditAnnouncementClick(bottomSheet.announcement.id)
                    },
                    onResendClick = {
                        activeBottomSheet = null
                        onResendAnnouncementClick(bottomSheet.announcement)
                    },
                    onReportClick = {
                        activeBottomSheet = NewsScreenBottomSheet.AnnouncementReportBottomSheet(bottomSheet.announcement)
                    },
                    onDeleteClick = {
                        activeBottomSheet = null
                        activeDialog = NewsDialog.DeleteAnnouncementDialog(bottomSheet.announcement)
                    },
                    onDismiss = { activeBottomSheet = null }
                )
            }

            is NewsScreenBottomSheet.AnnouncementReportBottomSheet -> {
                ReportBottomSheet(
                    items = AnnouncementReport.Reason.entries.map { stringResource(it.stringRes) },
                    onReportClick = { reason ->
                        activeBottomSheet = null
                        onReportAnnouncementClick(
                            AnnouncementReport(
                                announcementId = bottomSheet.announcement.id,
                                author = AnnouncementReport.Author(
                                    fullName = bottomSheet.announcement.author.fullName,
                                    email = bottomSheet.announcement.author.email
                                ),
                                reporter = AnnouncementReport.Reporter(
                                    fullName = user.fullName,
                                    email = user.email
                                ),
                                reason = reason
                            )
                        )
                    },
                    onDismiss = { activeBottomSheet = null }
                )
            }

            else -> Unit
        }
    }
}

private sealed class NewsScreenBottomSheet {
    data class AnnouncementBottomSheet(val announcement: Announcement): NewsScreenBottomSheet()
    data class AnnouncementReportBottomSheet(val announcement: Announcement): NewsScreenBottomSheet()
}

private sealed class NewsDialog {
    data class DeleteAnnouncementDialog(val announcement: Announcement): NewsDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun NewsScreenPreview() {
    GedoiseTheme {
        NewsScreen(
            user = userFixture,
            announcements = announcementsFixture,
            refreshing = false,
            loading = false,
            bottomBar = {},
            onRefresh = {},
            onAnnouncementClick = {},
            onResendAnnouncementClick = {},
            onEditAnnouncementClick = {},
            onDeleteAnnouncementClick = {},
            onCreateAnnouncementClick = {},
            onSeeAllAnnouncementsClick = {},
            onReportAnnouncementClick = {},
            onCreatePostClick = {}
        )
    }
}