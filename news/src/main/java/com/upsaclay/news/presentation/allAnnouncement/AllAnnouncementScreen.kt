package com.upsaclay.news.presentation.allAnnouncement

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.upsaclay.common.domain.entity.SingleUiEvent
import com.upsaclay.common.domain.entity.User
import com.upsaclay.common.domain.userFixture
import com.upsaclay.common.presentation.components.BackTopBar
import com.upsaclay.common.presentation.components.PullToRefreshComponent
import com.upsaclay.common.presentation.theme.GedoiseTheme
import com.upsaclay.common.presentation.theme.previewText
import com.upsaclay.common.utils.Phones
import com.upsaclay.common.utils.Tablets
import com.upsaclay.news.R
import com.upsaclay.news.domain.announcementsFixture
import com.upsaclay.news.domain.entity.Announcement
import com.upsaclay.news.domain.entity.AnnouncementState
import com.upsaclay.news.presentation.announcement.components.ShortAnnouncementItem
import com.upsaclay.common.R as commonR
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun AllAnnouncementDestination(
    onAnnouncementClick: (String) -> Unit,
    onBackClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
    viewModel: AllAnnouncementViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
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


    AllAnnouncementScreen(
        user = uiState.user,
        announcements = uiState.announcements,
        refreshing = uiState.refreshing,
        bottomBar = bottomBar,
        snackbarHostState = snackbarHostState,
        onRefresh = viewModel::refreshAnnouncements,
        onAnnouncementClick = onAnnouncementClick,
        onBackClick = onBackClick
    )
}

@Composable
private fun AllAnnouncementScreen(
    user: User?,
    announcements: List<Announcement>?,
    refreshing: Boolean,
    bottomBar: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState = SnackbarHostState(),
    onRefresh: () -> Unit,
    onAnnouncementClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    // TODO : augmenter la taille de l'item pour qu'il soit de taille moyenne
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = stringResource(id = commonR.string.app_name)
            )
        },
        bottomBar = bottomBar,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(it)
            }
        }

    ) {
        paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
        PullToRefreshComponent(
            onRefresh = onRefresh,
            isRefreshing = refreshing
        ) {
            Column {

                announcements?.let {
                    Text(
                        text = stringResource(id = R.string.all_annoucements),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(com.upsaclay.common.R.dimen.medium_padding))
                            .testTag(stringResource(id = R.string.news_screen_all_announcements_option_tag))
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (announcements.isEmpty()) {
                            item {
                                Text(
                                    modifier = Modifier.fillMaxWidth().testTag(stringResource(R.string.news_screen_empty_announcement_text_tag)),
                                    text = stringResource(id = R.string.no_announcement),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.previewText,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            items(announcements) { announcement ->
                                ShortAnnouncementItem(
                                    modifier = Modifier.testTag(stringResource(R.string.news_screen_recent_announcements_tag)),
                                    announcement = announcement,
                                    onClick = {
                                        if (announcement.state == AnnouncementState.PUBLISHED) {
                                            onAnnouncementClick(announcement.id)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
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
        AllAnnouncementScreen(
            user = userFixture,
            refreshing = false,
            announcements = announcementsFixture,
            bottomBar = {},
            onRefresh = {},
            onAnnouncementClick = {},
            onBackClick = {}
        )
    }
}