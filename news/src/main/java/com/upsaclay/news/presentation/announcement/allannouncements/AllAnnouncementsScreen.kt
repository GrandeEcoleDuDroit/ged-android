package com.upsaclay.news.presentation.announcement.allannouncements

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.extension.noRippleClickable
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.CircularProgressBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.EmptyText
import com.upsaclay.common.presentation.components.ListDivider
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
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
    var activeBottomSheet by remember { mutableStateOf<AllAnnouncementScreenBottomSheet?>(null) }
    var activeDialog by remember { mutableStateOf<AllAnnouncementDialog?>(null) }

    when(val dialog = activeDialog) {
        is AllAnnouncementDialog.DeleteAnnouncementDialog -> {
            DefaultDialog(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
                text = stringResource(id = R.string.delete_announcement_dialog_message),
                confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
                critical = true,
                onConfirm = {
                    activeDialog = null
                    onDeleteAnnouncementClick(dialog.announcement)
                },
                onCancel = { activeDialog = null }
            )
        }

        else -> Unit
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
        announcements?.let {
            PullToRefreshComponent(
                modifier = Modifier.padding(innerPadding),
                onRefresh = onRefresh,
                refreshing = refreshing
            ) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (announcements.isEmpty()) {
                        item {
                            EmptyText(text = stringResource(id = R.string.no_announcement))
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
                                            activeBottomSheet =
                                                AllAnnouncementScreenBottomSheet.AnnouncementBottomSheet(announcement)
                                        }
                                    }
                                    .padding(dimensionResource(com.upsaclay.common.R.dimen.medium_padding)),
                                announcement = announcement,
                                onOptionClick = {
                                    activeBottomSheet =
                                        AllAnnouncementScreenBottomSheet.AnnouncementBottomSheet(announcement)
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
        } ?: run {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressBar(
                    modifier = Modifier.padding(top = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                )
            }
        }

        when(val bottomSheet = activeBottomSheet) {
            is AllAnnouncementScreenBottomSheet.AnnouncementBottomSheet -> {
                AnnouncementBottomSheet(
                    announcementState = bottomSheet.announcement.state,
                    isEditable = user.admin && bottomSheet.announcement.author.id == user.id,
                    onEditClick = {
                        activeBottomSheet = null
                        onEditAnnouncementClick(bottomSheet.announcement)
                    },
                    onResendClick = {
                        activeBottomSheet = null
                        onResendAnnouncementClick(bottomSheet.announcement)
                    },
                    onReportClick = {
                        activeBottomSheet =
                            AllAnnouncementScreenBottomSheet.AnnouncementReportBottomSheet(bottomSheet.announcement)
                    },
                    onDeleteClick = {
                        activeBottomSheet = null
                        activeDialog = AllAnnouncementDialog.DeleteAnnouncementDialog(bottomSheet.announcement)
                    },
                    onDismiss = { activeBottomSheet = null }
                )
            }

            is AllAnnouncementScreenBottomSheet.AnnouncementReportBottomSheet -> {
                ReportBottomSheet(
                    items = AnnouncementReport.Reason.entries,
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

private sealed class AllAnnouncementScreenBottomSheet {
    data class AnnouncementBottomSheet(val announcement: Announcement): AllAnnouncementScreenBottomSheet()
    data class AnnouncementReportBottomSheet(val announcement: Announcement): AllAnnouncementScreenBottomSheet()
}

private sealed class AllAnnouncementDialog {
    data class DeleteAnnouncementDialog(val announcement: Announcement): AllAnnouncementDialog()
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
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