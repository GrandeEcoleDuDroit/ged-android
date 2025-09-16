package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.ListDivider
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.components.SensibleActionDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.presentation.announcement.components.AnnouncementBottomSheet
import com.upsaclay.news.presentation.announcement.components.ErrorAnnouncementBottomSheet
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

    if (uiState.announcements != null && uiState.user != null) {
        AllAnnouncementsScreen(
            user = uiState.user!!,
            announcements = uiState.announcements!!,
            loading = uiState.loading,
            refreshing = uiState.refreshing,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onAuthorClick = onAuthorClick,
            onRefresh = viewModel::refreshAnnouncements,
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
    announcements: List<Announcement>,
    loading: Boolean,
    refreshing: Boolean,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onBackClick: () -> Unit,
    onAuthorClick: (User) -> Unit,
    onRefresh: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onResendAnnouncementClick: (Announcement) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onReportAnnouncementClick: (AnnouncementReport) -> Unit,
    onDeleteAnnouncementClick: (Announcement) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }
    var announcementClicked by remember { mutableStateOf<Announcement?>(null) }
    var showReportBottomSheet by remember { mutableStateOf(false) }

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(R.string.all_announcements),
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            PullToRefreshComponent(
                onRefresh = onRefresh,
                isRefreshing = refreshing
            ) {
                LazyColumn {
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
                                announcement = announcement,
                                onClick = { onAnnouncementClick(announcement.id) },
                                onOptionClick = {
                                    announcementClicked = announcement
                                    showBottomSheet = true
                                },
                                onResendAnnouncementClick = {
                                    onResendAnnouncementClick(announcement)
                                },
                                onAuthorClick = { onAuthorClick(announcement.author) }
                            )

                            if (index == announcements.lastIndex) {
                                ListDivider()
                            }
                        }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        announcementClicked?.let { announcement ->
            when (announcement.state) {
                AnnouncementState.ERROR -> {
                    ErrorAnnouncementBottomSheet(
                        onResendClick = {
                            showBottomSheet = false
                            onResendAnnouncementClick(announcement)
                        },
                        onDeleteClick = {
                            showBottomSheet = false
                            onDeleteAnnouncementClick(announcement)
                        },
                        onDismiss = { showBottomSheet = false }
                    )
                }

                else -> {
                    AnnouncementBottomSheet(
                        isEditable = user.isMember && announcement.author.id == user.id,
                        onEditClick = {
                            showBottomSheet = false
                            announcementClicked?.let(onEditAnnouncementClick)
                        },
                        onReportClick = {
                            showBottomSheet = false
                            showReportBottomSheet = true
                        },
                        onDeleteClick = {
                            showBottomSheet = false
                            showDeleteAnnouncementDialog = true
                        },
                        onDismiss = { showBottomSheet = false }
                    )
                }
            }
        }
    }

    if (showReportBottomSheet) {
        ReportBottomSheet(
            items = AnnouncementReport.Reason.entries,
            onDismiss = { showReportBottomSheet = false },
            onReportClick = { reason ->
                showReportBottomSheet = false

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

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@Phones
@Composable
private fun AllAnnouncementsScreenPreview() {
    GedoiseTheme {
        AllAnnouncementsScreen (
            user = userFixture,
            announcements = announcementsFixture,
            loading = false,
            refreshing = false,
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