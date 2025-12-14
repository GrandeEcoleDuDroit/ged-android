package com.upsaclay.news.presentation.announcement.readannouncement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.extension.smallMediumSpacing
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.DefaultDialog
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.OptionButton
import com.upsaclay.common.presentation.components.ReportBottomSheet
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.PhonePreviews
import com.upsaclay.news.R
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementReport
import com.upsaclay.news.domain.longAnnouncementFixture
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.contentStyle
import com.upsaclay.news.presentation.announcement.AnnouncementPresentationUtils.titleStyle
import com.upsaclay.news.presentation.announcement.components.AnnouncementBottomSheet
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadAnnouncementDestination(
    announcementId: String,
    onBackClick: () -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onAuthorClick: (User) -> Unit,
    viewModel: ReadAnnouncementViewModel = koinViewModel(
        parameters = { parametersOf(announcementId) }
    )
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

                is ReadAnnouncementViewModel.ReadAnnouncementUiEvent.AnnouncementReported -> {
                    snackbarHostState.showSnackbar(
                        message = context.getString(event.messageId)
                    )
                }

                is ReadAnnouncementViewModel.ReadAnnouncementUiEvent.AnnouncementDeleted -> onBackClick()
            }
        }
    }

    if (uiState.user != null && uiState.announcement != null) {
        ReadAnnouncementScreen(
            user = uiState.user!!,
            announcement = uiState.announcement!!,
            loading = uiState.loading,
            snackbarHostState = snackbarHostState,
            onBackClick = onBackClick,
            onAuthorClick = onAuthorClick,
            onEditAnnouncementClick = onEditAnnouncementClick,
            onReportAnnouncementClick = viewModel::reportAnnouncement,
            onDeleteAnnouncementClick = viewModel::deleteAnnouncement
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadAnnouncementScreen(
    user: User,
    announcement: Announcement,
    loading: Boolean = false,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAuthorClick: (User) -> Unit,
    onEditAnnouncementClick: (Announcement) -> Unit,
    onReportAnnouncementClick: (AnnouncementReport) -> Unit,
    onDeleteAnnouncementClick: () -> Unit
) {
    var showDeleteAnnouncementDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showReportBottomSheet by remember { mutableStateOf(false) }

    if (showDeleteAnnouncementDialog) {
        DefaultDialog(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_delete_dialog_tag)),
            text = stringResource(id = R.string.delete_announcement_dialog_message),
            confirmText = stringResource(id = com.upsaclay.common.R.string.delete),
            critical = true,
            onConfirm = {
                showDeleteAnnouncementDialog = false
                onDeleteAnnouncementClick()
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
                title = stringResource(id = R.string.announcement),
                leadingIcon = {
                    OptionButton { showBottomSheet = true }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
            ) {
               Snackbar(
                   snackbarData = it,
                   modifier = Modifier.testTag(stringResource(id = R.string.read_screen_snackbar_tag))
               )
            }
        }
    ) { innerPadding ->
        SelectionContainer {
            Announcement(
                modifier = Modifier
                    .fillMaxSize()
                    .rootMediumPadding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                announcement = announcement,
                onAuthorClick = { onAuthorClick(announcement.author) }
            )
        }

        if (showBottomSheet) {
            AnnouncementBottomSheet(
                announcementState = announcement.state,
                isEditable = user.admin && user.id == announcement.author.id,
                onEditClick = {
                    showBottomSheet = false
                    onEditAnnouncementClick(announcement)
                },
                onResendClick = {},
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

        if (showReportBottomSheet) {
            ReportBottomSheet(
                items = AnnouncementReport.Reason.entries,
                onDismiss = { showReportBottomSheet = false },
                onReportClick = { reason ->
                    showReportBottomSheet = false
                    onReportAnnouncementClick(
                        AnnouncementReport(
                            announcementId = announcement.id,
                            author = AnnouncementReport.Author(
                                fullName = announcement.author.fullName,
                                email = announcement.author.email
                            ),
                            reporter = AnnouncementReport.Reporter(
                                fullName = user.fullName,
                                email = user.email
                            ),
                            reason = reason
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun Announcement(
    modifier: Modifier = Modifier,
    announcement: Announcement,
    onAuthorClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.smallMediumSpacing()
    ) {
        AnnouncementHeader(
            announcement = announcement,
            onClick = onAuthorClick
        )

        announcement.title?.let {
            Text(
                modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_title_tag)),
                text = it,
                style = titleStyle
            )
        }

        Text(
            modifier = Modifier.testTag(stringResource(id = R.string.read_screen_announcement_content_tag)),
            text = announcement.content,
            style = contentStyle
        )
    }
}

/*
 =====================================================================
                                Preview
 =====================================================================
 */

@PhonePreviews
@Composable
private fun NonEditableAnnouncementScreenPreview() {
    GedoiseTheme {
        Surface {
            ReadAnnouncementScreen(
                user = longAnnouncementFixture.author,
                announcement = longAnnouncementFixture,
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                onAuthorClick = {},
                onEditAnnouncementClick = {},
                onReportAnnouncementClick = {},
                onDeleteAnnouncementClick = {}
            )
        }
    }
}

@PhonePreviews
@Composable
private fun EditableAnnouncementScreenPreview() {
    GedoiseTheme {
        Surface {
            ReadAnnouncementScreen(
                user = longAnnouncementFixture.author,
                announcement = longAnnouncementFixture,
                snackbarHostState = SnackbarHostState(),
                onBackClick = {},
                onAuthorClick = {},
                onEditAnnouncementClick = {},
                onReportAnnouncementClick = {},
                onDeleteAnnouncementClick = {}
            )
        }
    }
}