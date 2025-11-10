package com.upsaclay.news.presentation.announcement.editannouncement

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.SingleUiEvent
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.presentation.announcement.components.EditAnnouncementInputs
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditAnnouncementDestination(
    announcement: Announcement,
    onBackClick: () -> Unit,
    viewModel: EditAnnouncementViewModel = koinViewModel(
        parameters = { parametersOf(announcement) }
    )
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is SingleUiEvent.Error -> scope.launch {
                    snackbarHostState.showSnackbar(context.getString(event.messageId))
                }

                is SingleUiEvent.Success -> onBackClick()
            }
        }
    }

    EditAnnouncementScreen(
        title = uiState.title,
        content = uiState.content,
        loading = uiState.loading,
        updateEnabled = uiState.updateEnabled,
        snackbarHostState = snackbarHostState,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = onBackClick,
        onSaveAnnouncementClick = viewModel::updateAnnouncement
    )
}

@Composable
private fun EditAnnouncementScreen(
    title: String,
    content: TextFieldValue,
    loading: Boolean,
    updateEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
    onTitleChange: (String) -> Unit,
    onContentChange: (TextFieldValue) -> Unit,
    onBackClick: () -> Unit,
    onSaveAnnouncementClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    if (loading) {
        LoadingDialog()
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            EditTopBar(
                title = stringResource(id = R.string.edit_announcement),
                onCancelClick = {
                    focusManager.clearFocus()
                    onBackClick()
                },
                onActionClick = {
                    focusManager.clearFocus()
                    onSaveAnnouncementClick()
                },
                buttonEnable = updateEnabled && !loading,
                actionLabel = stringResource(id = com.upsaclay.common.R.string.save)
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    modifier = Modifier.testTag(stringResource(R.string.edit_screen_snackbar_tag)),
                    snackbarData = it
                )
            }
        }
    ) { innerPadding ->
        EditAnnouncementInputs(
            modifier = Modifier.rootMediumPadding(innerPadding),
            title = title,
            content = content,
            onTitleChange = onTitleChange,
            onContentChange = onContentChange
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
private fun EditAnnouncementScreenPreview() {
    var title by remember { mutableStateOf(announcementFixture.title ?: "") }
    var content by remember {
        mutableStateOf(
            TextFieldValue(
                text = announcementFixture.content
            )
        )
    }
    GedoiseTheme {
        Surface {
            EditAnnouncementScreen(
                title = title,
                content = content,
                loading = false,
                updateEnabled = false,
                snackbarHostState = SnackbarHostState(),
                onTitleChange = { title = it },
                onContentChange = { content = it },
                onBackClick = {},
                onSaveAnnouncementClick = {}
            )
        }
    }
}