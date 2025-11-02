package com.upsaclay.news.presentation.announcement.createannouncement

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import com.upsaclay.common.extension.rootMediumPadding
import com.upsaclay.common.presentation.components.EditTopBar
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.utils.Phones
import com.upsaclay.news.R
import com.upsaclay.news.presentation.announcement.components.CreateAnnouncementInputs
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateAnnouncementDestination(
    onBackClick: () -> Unit,
    viewModel: CreateAnnouncementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateAnnouncementScreen(
        title = uiState.title,
        content = uiState.content,
        createEnabled = uiState.createEnabled,
        onTitleChange = viewModel::onTitleChange,
        onContentChange = viewModel::onContentChange,
        onBackClick = onBackClick,
        onCreateAnnouncementClick = {
            viewModel.createAnnouncement()
            onBackClick()
        }
    )
}

@Composable
private fun CreateAnnouncementScreen(
    title: String,
    content: String,
    createEnabled: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCreateAnnouncementClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            EditTopBar(
                actionLabel = stringResource(id = com.upsaclay.common.R.string.publish),
                title = stringResource(id = R.string.new_announcement),
                onCancelClick = {
                    focusManager.clearFocus()
                    onBackClick()
                },
                onActionClick = {
                    focusManager.clearFocus()
                    onCreateAnnouncementClick()
                },
                buttonEnable = createEnabled
            )
        }
    ) { innerPadding ->
        CreateAnnouncementInputs(
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
private fun CreateAnnouncementScreenPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        CreateAnnouncementScreen(
            title = title,
            content = content,
            createEnabled = false,
            onTitleChange = { title = it },
            onContentChange = { content = it },
            onBackClick = {},
            onCreateAnnouncementClick = {}
        )
    }
}