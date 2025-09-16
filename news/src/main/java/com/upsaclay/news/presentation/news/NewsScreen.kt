package com.upsaclay.news.presentation.news

import androidx.compose.foundation.layout.Box
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
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.Tablets
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.presentation.announcement.components.AnnouncementBottomSheet
import com.upsaclay.news.presentation.announcement.components.ErrorAnnouncementBottomSheet
import com.upsaclay.news.presentation.news.components.NewsScaffold
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewsDestination(
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementClick: () -> Unit,
    onProfilePictureClick: () -> Unit,
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
            onProfilePictureClick = onProfilePictureClick,
            onRefresh = viewModel::refreshAnnouncements,
            onAnnouncementClick = onAnnouncementClick,
            onCreateAnnouncementClick = onCreateAnnouncementClick,
            onResendAnnouncementClick = viewModel::resendAnnouncement,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onDeleteAnnouncementClick = viewModel::deleteAnnouncement,
            onSeeAllAnnouncementClick = onSeeAllAnnouncementClick,
            onReportAnnouncementClick = viewModel::reportAnnouncement
        )
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
    onProfilePictureClick: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onCreateAnnouncementClick: () -> Unit,
    onResendAnnouncementClick: (Announcement) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onDeleteAnnouncementClick: (Announcement) -> Unit,
    onSeeAllAnnouncementClick: () -> Unit,
    onReportAnnouncementClick: (AnnouncementReport) -> Unit
) {
    var showAnnouncementBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }
    var announcementClicked by remember { mutableStateOf<Announcement?>(null) }
    var showAnnouncementReportBottomSheet by remember { mutableStateOf(false) }

    if (showDeleteAnnouncementDialog) {
        SensibleActionDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
            title = stringResource(id = R.string.delete_announcement_dialog_title),
            text = stringResource(id = R.string.delete_announcement_dialog_text),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            onConfirm = {
                showDeleteAnnouncementDialog = false
                announcementClicked?.let(onDeleteAnnouncementClick)
            },
            onCancel = { showDeleteAnnouncementDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    NewsScaffold(
        user = user,
        onCreateAnnouncementClick = onCreateAnnouncementClick,
        onProfilePictureClick = onProfilePictureClick,
        snackbarHostState = snackbarHostState,
        bottomBar = bottomBar
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PullToRefreshComponent(
                onRefresh = onRefresh,
                isRefreshing = refreshing
            ) {
                Column {
                    announcements?.let {
                        RecentAnnouncementSection(
                            modifier = Modifier.weight(1f),
                            announcements = it,
                            onAnnouncementClick = onAnnouncementClick,
                            onUncreatedAnnouncementClick = { announcement ->
                                announcementClicked = announcement
                                showAnnouncementBottomSheet = true
                            },
                            onSeeAllAnnouncementClick = onSeeAllAnnouncementClick,
                            onAnnouncementOptionClick = { announcement ->
                                announcementClicked = announcement
                                showAnnouncementBottomSheet = true
                            }
                        )
                    }
                }
            }

            if (showAnnouncementBottomSheet) {
                announcementClicked?.let { announcement ->
                    when (announcement.state) {
                        AnnouncementState.ERROR -> {
                            ErrorAnnouncementBottomSheet(
                                onResendClick = {
                                    showAnnouncementBottomSheet = false
                                    onResendAnnouncementClick(announcement)
                                },
                                onDeleteClick = {
                                    showAnnouncementBottomSheet = false
                                    onDeleteAnnouncementClick(announcement)
                                },
                                onDismiss = { showAnnouncementBottomSheet = false }
                            )
                        }

                        else -> {
                            AnnouncementBottomSheet(
                                isEditable = user.isMember && announcement.author.id == user.id,
                                onEditClick = {
                                    showAnnouncementBottomSheet = false
                                    announcementClicked?.let(onEditAnnouncementClick)
                                },
                                onReportClick = {
                                    showAnnouncementBottomSheet = false
                                    showAnnouncementReportBottomSheet = true
                                },
                                onDeleteClick = {
                                    showAnnouncementBottomSheet = false
                                    showDeleteAnnouncementDialog = true
                                },
                                onDismiss = { showAnnouncementBottomSheet = false }
                            )
                        }
                    }
                }
            }

            if (showAnnouncementReportBottomSheet) {
                ReportBottomSheet(
                    items = AnnouncementReport.Reason.entries,
                    onDismiss = { showAnnouncementReportBottomSheet = false },
                    onReportClick = { reason ->
                        showAnnouncementReportBottomSheet = false

                        announcementClicked?.let { announcement ->
                            onReportAnnouncementClick(
                                AnnouncementReport(
                                    announcementId = announcement.id,
                                    userInfo = AnnouncementReport.UserInfo(
                                        fullName = user.fullName,
                                        email = user.email
                                    ),
                                    authorInfo = AnnouncementReport.UserInfo(
                                        fullName = announcement.author.fullName,
                                        email = announcement.author.email
                                    ),
                                    reason = reason,
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Tablets
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
            onProfilePictureClick = {},
            onAnnouncementClick = {},
            onResendAnnouncementClick = {},
            onEditAnnouncementClick = {},
            onDeleteAnnouncementClick = {},
            onCreateAnnouncementClick = {},
            onSeeAllAnnouncementClick = {},
            onReportAnnouncementClick = {}
        )
    }
}