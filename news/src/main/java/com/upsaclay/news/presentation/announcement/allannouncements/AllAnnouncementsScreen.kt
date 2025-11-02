package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.ListDivider
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.Announcement.AnnouncementState
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.presentation.announcement.components.AnnouncementBottomSheet
import com.upsaclay.news.presentation.announcement.components.ExtendedAnnouncementItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllAnnouncementsDestination(
    onBackClick: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onAuthorClick: (User) -> Unit,
    viewModel: AllAnnouncementsViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageId)
                    )
                }

                is SingleUiEvent.Success -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageId)
                    )
                }
            }
        }
    }

    if (uiState.user != null) {
        AllAnnouncementsScreen(
            user = uiState.user!!,
            announcements = uiState.announcements,
            refreshing = uiState.refreshing,
            loading = uiState.loading,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onRefresh = viewModel::refreshAnnouncements,
            onAuthorClick = onAuthorClick,
            onAnnouncementClick = onAnnouncementClick,
            onResendAnnouncementClick = viewModel::resendAnnouncement,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onReportAnnouncementClick = viewModel::reportAnnouncement,
            onDeleteAnnouncementClick = viewModel::deleteAnnouncement
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllAnnouncementsScreen(
    user: User,
    announcements: List<Announcement>?,
    refreshing: Boolean,
    loading: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onAuthorClick: (User) -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onResendAnnouncementClick: (Announcement) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onReportAnnouncementClick: (AnnouncementReport) -> Unit,
    onDeleteAnnouncementClick: (Announcement) -> Unit
) {
    var showAnnouncementBottomSheet by remember { mutableStateOf(false) }
    var showAnnouncementReportBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }
    var clickedAnnouncement by remember { mutableStateOf<Announcement?>(null) }

    if (showDeleteAnnouncementDialog) {
        DefaultDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
            text = stringResource(id = R.string.delete_announcement_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            critical = true,
            onConfirm = {
                showDeleteAnnouncementDialog = false
                clickedAnnouncement?.let(onDeleteAnnouncementClick)
            },
            onCancel = { showDeleteAnnouncementDialog = false }
        )
    }

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.all_announcements)
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { innerPadding ->
        PullToRefreshComponent(
            modifier = Modifier.padding(innerPadding),
            onRefresh = onRefresh,
            refreshing = refreshing
        ) {
            announcements?.let {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (announcements.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.no_announcement),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.previewText,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        itemsIndexed(announcements) { index, announcement ->
                            ListDivider()

                            ExtendedAnnouncementItem(
                                modifier = Modifier
                                    .noRippleClickable {
                                        if (announcement.state == AnnouncementState.PUBLISHED) {
                                            onAnnouncementClick(announcement.id)
                                        } else {
                                            clickedAnnouncement = announcement
                                            showAnnouncementBottomSheet = true
                                        }
                                    }
                                    .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                                announcement = announcement,
                                onOptionClick = {
                                    clickedAnnouncement = announcement
                                    showAnnouncementBottomSheet = true
                                },
                                onAuthorClick = { onAuthorClick(announcement.author) }
                            )

                            if (index == announcements.lastIndex) {
                                ListDivider()
                            }
                        }
                    }
                }
            } ?: run {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressBar(
                        modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                    )
                }
            }
        }
    }

    if (showAnnouncementBottomSheet) {
        clickedAnnouncement?.let { announcement ->
            AnnouncementBottomSheet(
                announcement = announcement,
                isEditable = user.admin && announcement.author.id == user.id,
                onEditClick = {
                    showAnnouncementBottomSheet = false
                    clickedAnnouncement?.let(onEditAnnouncementClick)
                },
                onResendClick = {
                    showAnnouncementBottomSheet = false
                    onResendAnnouncementClick(announcement)
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

    if (showAnnouncementReportBottomSheet) {
        ReportBottomSheet(
            items = AnnouncementReport.Reason.entries,
            onDismiss = { showAnnouncementReportBottomSheet = false },
            onReportClick = { reason ->
                showAnnouncementReportBottomSheet = false

                clickedAnnouncement?.let { announcement ->
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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AllAnnouncementsScreenPreview() {
    GedoiseTheme {
        AllAnnouncementsScreen(
            user = userFixture,
            announcements = announcementsFixture,
            refreshing = false,
            loading = false,
            onBackClick = {},
            onAuthorClick = {},
            onRefresh = {},
            onAnnouncementClick = {},
            onResendAnnouncementClick = {},
            onEditAnnouncementClick = {},
            onReportAnnouncementClick = {},
            onDeleteAnnouncementClick = {}
        )
    }
}