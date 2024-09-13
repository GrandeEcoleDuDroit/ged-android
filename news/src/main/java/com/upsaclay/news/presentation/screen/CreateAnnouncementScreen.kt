package com.upsaclay.news.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.upsaclay.common.domain.model.User
import com.upsaclay.common.presentation.components.LoadingDialog
import com.upsaclay.common.presentation.components.SmallTopBarEdit
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.spacing
import com.upsaclay.common.utils.showToast
import com.upsaclay.news.R
import com.upsaclay.news.domain.model.Announcement
import com.upsaclay.news.domain.model.AnnouncementState
import com.upsaclay.news.presentation.components.TransparentFocusedTextField
import com.upsaclay.news.presentation.components.TransparentTextField
import com.upsaclay.news.presentation.viewmodel.CreateAnnouncementViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDateTime

@Composable
fun CreateAnnouncementScreen(
    navController: NavController,
    createAnnouncementViewModel: CreateAnnouncementViewModel = koinViewModel(),
    user: User
) {
    val state = createAnnouncementViewModel.announcementState.collectAsState(AnnouncementState.DEFAULT).value
    val title: String = createAnnouncementViewModel.title
    val content: String = createAnnouncementViewModel.content
    val context = LocalContext.current
    var showLoadingDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state) {
        when (state) {
            AnnouncementState.ANNOUNCEMENT_CREATION_ERROR -> {
                showLoadingDialog = false
                showToast(context, R.string.announcement_creation_error)
            }

            AnnouncementState.ANNOUNCEMENT_CREATED -> {
                showLoadingDialog = false
                navController.popBackStack()
            }

            AnnouncementState.LOADING -> showLoadingDialog = true

            else -> {}
        }
    }

    if(showLoadingDialog) {
        LoadingDialog(text = stringResource(id = com.upsaclay.common.R.string.loading))
    }

    Scaffold(
        topBar = {
            SmallTopBarEdit(
                title = "",
                confirmText = stringResource(id = com.upsaclay.common.R.string.publish),
                onCancelClick = { navController.popBackStack() },
                onSaveClick = {
                    createAnnouncementViewModel.createAnnouncement(
                        Announcement(
                            id = -1,
                            title = if(title.isBlank()) null else title.trim(),
                            content = content.trim(),
                            date = LocalDateTime.now(),
                            author = user
                        )
                    )
                },
                isButtonEnable = content.isNotBlank()
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    start = MaterialTheme.spacing.medium,
                    end = MaterialTheme.spacing.medium,
                    bottom = MaterialTheme.spacing.medium
                )
                .fillMaxSize()
        ) {
            Column {
                TransparentFocusedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    defaultValue = title,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field_entry),
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    onValueChange = { createAnnouncementViewModel.updateTitle(it) },
                    textStyle = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                TransparentTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = content,
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field_entry),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    onValueChange = { createAnnouncementViewModel.updateContent(it) },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
private fun CreateAnnouncementScreenPreview() {
    val title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    GedoiseTheme {
        Scaffold (
            topBar = {
                SmallTopBarEdit(
                    onCancelClick = { },
                    onSaveClick = { },
                    confirmText = stringResource(id = com.upsaclay.common.R.string.publish)
                )
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(
                        top = contentPadding.calculateTopPadding(),
                        start = MaterialTheme.spacing.medium,
                        end = MaterialTheme.spacing.medium,
                        bottom = MaterialTheme.spacing.medium
                    )
                    .fillMaxSize()
            ) {
                TransparentFocusedTextField(
                    defaultValue = title,
                    onValueChange = { },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.title_field_entry),
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    textStyle = MaterialTheme.typography.titleMedium,
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

                TransparentTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.content_field_entry),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}